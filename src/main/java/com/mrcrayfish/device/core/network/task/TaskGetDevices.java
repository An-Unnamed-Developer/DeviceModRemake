package com.mrcrayfish.device.core.network.task;

import com.mrcrayfish.device.Reference;
import com.mrcrayfish.device.api.annotation.DeviceTask;
import com.mrcrayfish.device.api.task.Task;
import com.mrcrayfish.device.core.network.NetworkDevice;
import com.mrcrayfish.device.core.network.Router;
import com.mrcrayfish.device.tileentity.TileEntityNetworkDevice;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;

/**
 * Author: MrCrayfish
 */
@DeviceTask(modId = Reference.MOD_ID, taskId = "get_network_devices")
public class TaskGetDevices extends Task
{
    private BlockPos devicePos;
    private Class<? extends TileEntityNetworkDevice> targetDeviceClass;

    private Collection<NetworkDevice> foundDevices;

    private TaskGetDevices() {}

    public TaskGetDevices(BlockPos devicePos)
    {
        this.devicePos = devicePos;
    }

    public TaskGetDevices(BlockPos devicePos, Class<? extends TileEntityNetworkDevice> targetDeviceClass)
    {
        this();
        this.devicePos = devicePos;
        this.targetDeviceClass = targetDeviceClass;
    }

    @Override
    public void prepareRequest(NBTTagCompound nbt)
    {
        nbt.setLong("devicePos", devicePos.toLong());
        if(targetDeviceClass != null)
        {
            nbt.setString("targetClass", targetDeviceClass.getName());
        }
    }

    @Override
    public void processRequest(NBTTagCompound nbt, World world, EntityPlayer player)
    {
        BlockPos devicePos = BlockPos.fromLong(nbt.getLong("devicePos"));
        Class targetDeviceClass = null;
        try
        {
            Class targetClass = Class.forName(nbt.getString("targetClass"));
            if(TileEntityNetworkDevice.class.isAssignableFrom(targetClass))
            {
                targetDeviceClass = targetClass;
            }
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        TileEntity tileEntity = world.getTileEntity(devicePos);
        if(tileEntity instanceof TileEntityNetworkDevice)
        {
            TileEntityNetworkDevice tileEntityNetworkDevice = (TileEntityNetworkDevice) tileEntity;
            if(tileEntityNetworkDevice.isConnected())
            {
                Router router = tileEntityNetworkDevice.getRouter();
                if(router != null)
                {
                    if(targetDeviceClass != null)
                    {
                        foundDevices = router.getConnectedDevices(world, targetDeviceClass);
                    }
                    else
                    {
                        foundDevices = router.getConnectedDevices(world);
                    }
                    this.setSuccessful();
                }
            }
        }
    }

    @Override
    public void prepareResponse(NBTTagCompound nbt)
    {
        if(this.isSucessful())
        {
            NBTTagList deviceList = new NBTTagList();
            foundDevices.forEach(device -> deviceList.appendTag(device.toTag(true)));
            nbt.setTag("network_devices", deviceList);
        }
    }

    @Override
    public void processResponse(NBTTagCompound nbt)
    {

    }
}
