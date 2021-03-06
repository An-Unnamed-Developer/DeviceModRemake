package com.mrcrayfish.device.core.network.task;

import com.mrcrayfish.device.Reference;
import com.mrcrayfish.device.api.annotation.DeviceTask;
import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.tileentity.TileEntityNetworkDevice;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Author: MrCrayfish
 */
@DeviceTask(modId = Reference.MOD_ID, taskId = "ping")
public class TaskPing extends Task
{
    private BlockPos sourceDevicePos;
    private int strength;

    private TaskPing() {}

    public TaskPing(BlockPos sourceDevicePos)
    {
        this();
        this.sourceDevicePos = sourceDevicePos;
    }

    @Override
    public void prepareRequest(NBTTagCompound nbt)
    {
        nbt.setLong("sourceDevicePos", sourceDevicePos.toLong());
    }

    @Override
    public void processRequest(NBTTagCompound nbt, World world, EntityPlayer player)
    {
        TileEntity tileEntity = world.getTileEntity(BlockPos.fromLong(nbt.getLong("sourceDevicePos")));
        if(tileEntity instanceof TileEntityNetworkDevice)
        {
            TileEntityNetworkDevice tileEntityNetworkDevice = (TileEntityNetworkDevice) tileEntity;
            if(tileEntityNetworkDevice.isConnected())
            {
                this.strength = tileEntityNetworkDevice.getSignalStrength();
                this.setSuccessful();
            }
        }
    }

    @Override
    public void prepareResponse(NBTTagCompound nbt)
    {
        if(this.isSucessful())
        {
            nbt.setInteger("strength", strength);
        }
    }

    @Override
    public void processResponse(NBTTagCompound nbt)
    {

    }
}
