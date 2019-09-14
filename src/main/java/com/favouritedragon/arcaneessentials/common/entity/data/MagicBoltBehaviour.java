package com.favouritedragon.arcaneessentials.common.entity.data;

import com.favouritedragon.arcaneessentials.common.entity.EntityMagicBolt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

public abstract class MagicBoltBehaviour extends Behaviour<EntityMagicBolt> {
	public static final DataSerializer<MagicBoltBehaviour> DATA_SERIALIZER = new Behaviour.BehaviorSerializer<>();


	public static void register() {
		DataSerializers.registerSerializer(DATA_SERIALIZER);
		registerBehaviour(Idle.class);
	}

	public static class Idle extends MagicBoltBehaviour {

		@Override
		public Behaviour onUpdate(EntityMagicBolt entity) {
			return this;
		}

		@Override
		public void fromBytes(PacketBuffer buf) {

		}

		@Override
		public void toBytes(PacketBuffer buf) {

		}

		@Override
		public void load(NBTTagCompound nbt) {

		}

		@Override
		public void save(NBTTagCompound nbt) {

		}
	}

}
