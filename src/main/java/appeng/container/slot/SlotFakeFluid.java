/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.container.slot;

import java.util.Collections;

import appeng.api.storage.data.IAEFluidStack;
import appeng.client.me.FluidRepo;
import appeng.client.me.InternalFluidSlotME;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketFluidSlot;
import appeng.fluids.container.slots.IMEFluidSlot;
import appeng.fluids.util.AEFluidInventory;
import appeng.fluids.util.AEFluidStack;
import appeng.fluids.util.IAEFluidTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandler;


public class SlotFakeFluid extends AppEngSlot
{
	private final IAEFluidTank fakeFluids;
	private final int index;

	public SlotFakeFluid( final AEFluidInventory inv, final int idx, final int x, final int y )
	{
		super( null, idx, x, y );
		this.index = idx;
		this.fakeFluids = inv;
	}

	public IAEFluidStack getFluidStack()
	{
		return this.fakeFluids.getFluidInSlot( this.index );
	}

	public void setFluidStack( final IAEFluidStack stack )
	{
		this.fakeFluids.setFluidInSlot( this.index, stack );
		NetworkHandler.instance().sendToServer( new PacketFluidSlot( Collections.singletonMap( this.index, this.getFluidStack() ) ) );
	}

	@Override
	public ItemStack getStack()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean getHasStack()
	{
		if( this.getFluidStack() != null )
		{
			return true;
		}
		return false;
	}

	@Override
	public ItemStack onTake( final EntityPlayer par1EntityPlayer, final ItemStack par2ItemStack )
	{
		return par2ItemStack;
	}

	@Override
	public ItemStack decrStackSize( final int par1 )
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isItemValid( final ItemStack par1ItemStack )
	{
		return false;
	}

	@Override
	public void putStack( ItemStack is )
	{
		if( is.isEmpty() )
		{
			this.setFluidStack( null );
		}
		else
		{
			final FluidStack fluid = FluidUtil.getFluidContained( is );
			if( fluid != null )
			{
				this.setFluidStack( AEFluidStack.fromFluidStack( fluid ) );
			}
		}
	}

	@Override
	public boolean canTakeStack( final EntityPlayer par1EntityPlayer )
	{
		return false;
	}
}
