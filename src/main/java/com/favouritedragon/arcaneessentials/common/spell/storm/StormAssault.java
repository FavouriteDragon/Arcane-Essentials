package com.favouritedragon.arcaneessentials.common.spell.storm;

import com.favouritedragon.arcaneessentials.common.spell.IArcaneSpell;
import electroblob.wizardry.data.IVariable;
import electroblob.wizardry.data.Persistence;
import electroblob.wizardry.data.WizardData;
import electroblob.wizardry.registry.Spells;
import electroblob.wizardry.registry.WizardryItems;
import electroblob.wizardry.spell.Charge;
import electroblob.wizardry.spell.Spell;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.SpellModifiers;
import electroblob.wizardry.util.WizardryUtilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class StormAssault extends Spell implements IArcaneSpell {

	private static final IVariable<Integer> ASSAULT_TIME = new IVariable.Variable<Integer>(Persistence.NEVER).withTicker(StormAssault::update);
	private static final IVariable<SpellModifiers> CHARGE_MODIFIERS = new IVariable.Variable<>(Persistence.NEVER);

	private static final String NUMBER_OF_STRIKES = "strike_count";

	private static final double EXTRA_HIT_MARGIN = 1;

	public StormAssault(){
		super("charge", EnumAction.NONE, false);
		addProperties(NUMBER_OF_STRIKES, DURATION, DAMAGE, EFFECT_STRENGTH);
		this.soundValues(0.6f, 1, 0);
	}

	@Override
	public boolean cast(World world, EntityPlayer caster, EnumHand hand, int ticksInUse, SpellModifiers modifiers){

		WizardData.get(caster).setVariable(ASSAULT_TIME, (int)(getProperty(DURATION).floatValue()
				* modifiers.get(WizardryItems.duration_upgrade)));

		WizardData.get(caster).setVariable(CHARGE_MODIFIERS, modifiers);

		if(world.isRemote) world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, caster.posX, caster.posY + caster.height/2, caster.posZ, 0, 0, 0);

		this.playSound(world, caster, ticksInUse, -1, modifiers);

		return true;
	}

	private static int update(EntityPlayer player, Integer chargeTime){

		if(chargeTime == null) chargeTime = 0;

		if(chargeTime > 0){

			SpellModifiers modifiers = WizardData.get(player).getVariable(CHARGE_MODIFIERS);
			if(modifiers == null) modifiers = new SpellModifiers();

			Vec3d look = player.getLookVec();

			float speed = Spells.charge.getProperty(Charge.CHARGE_SPEED).floatValue() * modifiers.get(WizardryItems.range_upgrade);

			player.motionX = look.x * speed;
			player.motionZ = look.z * speed;

			if(player.world.isRemote){
				for(int i = 0; i < 5; i++){
					ParticleBuilder.create(ParticleBuilder.Type.SPARK, player).spawn(player.world);
				}
			}

			List<EntityLivingBase> collided = player.world.getEntitiesWithinAABB(EntityLivingBase.class, player.getEntityBoundingBox().grow(EXTRA_HIT_MARGIN));

			collided.remove(player);

			float damage = Spells.charge.getProperty(DAMAGE).floatValue() * modifiers.get(SpellModifiers.POTENCY);
			float knockback = Spells.charge.getProperty(EFFECT_STRENGTH).floatValue();

			collided.forEach(e -> e.attackEntityFrom(MagicDamage.causeDirectMagicDamage(player, MagicDamage.DamageType.SHOCK), damage));
			collided.forEach(e -> e.addVelocity(player.motionX * knockback, player.motionY * knockback + 0.3f, player.motionZ * knockback));

			if(player.world.isRemote) player.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE,
					player.posX + player.motionX, player.posY + player.height/2, player.posZ + player.motionZ, 0, 0, 0);

			if(collided.isEmpty()) chargeTime--;
			else{
				WizardryUtilities.playSoundAtPlayer(player, SoundEvents.ENTITY_GENERIC_HURT, 1, 1);
				chargeTime = 0;
			}
		}

		return chargeTime;
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onLivingAttackEvent(LivingAttackEvent event){
		// Players are immune to melee damage while charging
		if(event.getEntity() instanceof EntityPlayer && event.getSource().getTrueSource() instanceof EntityLivingBase){

			EntityPlayer player = (EntityPlayer)event.getEntity();
			EntityLivingBase attacker = (EntityLivingBase)event.getSource().getTrueSource();

			if(WizardData.get(player) != null){

				Integer chargeTime = WizardData.get(player).getVariable(ASSAULT_TIME);

				if(chargeTime != null && chargeTime > 0
						&& player.getEntityBoundingBox().grow(EXTRA_HIT_MARGIN).intersects(attacker.getEntityBoundingBox())){
					event.setCanceled(true);
				}
			}
		}
	}

	@Override
	public boolean isSwordCastable() {
		return true;
	}
}
