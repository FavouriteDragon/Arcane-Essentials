package com.favouritedragon.arcaneessentials.common.entity.data;

import com.favouritedragon.arcaneessentials.common.entity.EntityMagicBolt;
import com.favouritedragon.arcaneessentials.common.entity.EntityMagicConstruct;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

public abstract class MagicConstructBehaviour extends Behaviour<EntityMagicConstruct> {
	public static final DataSerializer<MagicConstructBehaviour> DATA_SERIALIZER = new Behaviour.BehaviorSerializer<>();


	public static void register() {
		DataSerializers.registerSerializer(DATA_SERIALIZER);
		registerBehaviour(MagicConstructBehaviour.Idle.class);
	}

	public static class Idle extends MagicConstructBehaviour {

		@Override
		public Behaviour onUpdate(EntityMagicConstruct entity) {
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
