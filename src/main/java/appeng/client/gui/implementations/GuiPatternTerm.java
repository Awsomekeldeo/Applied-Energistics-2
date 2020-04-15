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

package appeng.client.gui.implementations;


import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import appeng.api.config.ActionItems;
import appeng.api.config.ItemPatternMode;
import appeng.api.config.FluidPatternMode;
import appeng.api.config.ItemSubstitution;
import appeng.api.config.Settings;
import appeng.api.storage.ITerminalHost;
import appeng.fluids.block.BlockFluidInterface;
import appeng.client.gui.widgets.GuiImgButton;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.container.implementations.ContainerPatternTerm;
import appeng.container.slot.AppEngSlot;
import appeng.core.AELog;
import appeng.core.localization.GuiText;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketValueConfig;


public class GuiPatternTerm extends GuiMEMonitorable
{

	private static final String BACKGROUND_CRAFTING_MODE = "guis/pattern.png";
	private static final String BACKGROUND_PROCESSING_MODE = "guis/pattern2.png";

	private static final String SUBSITUTION_DISABLE = "0";
	private static final String SUBSITUTION_ENABLE = "1";

	private static final String CRAFTMODE_CRAFTING = "0";
	private static final String CRAFTMODE_ITEM_PROCESSING = "1";
	private static final String CRAFTMODE_FLUID_PROCESSING = "2";

	private final ContainerPatternTerm container;

	private GuiTabButton tabCraftButton;
	private GuiTabButton tabProcessButton;
	private GuiTabButton tabFluidProcessButton;
	private GuiImgButton itemPatternModeIIBtn;
	private GuiImgButton itemPatternModeIFBtn;
	private GuiImgButton fluidPatternModeFFBtn;
	private GuiImgButton fluidPatternModeFIBtn;
	private GuiImgButton substitutionsEnabledBtn;
	private GuiImgButton substitutionsDisabledBtn;
	private GuiImgButton encodeBtn;
	private GuiImgButton clearBtn;

	public GuiPatternTerm( final InventoryPlayer inventoryPlayer, final ITerminalHost te )
	{
		super( inventoryPlayer, te, new ContainerPatternTerm( inventoryPlayer, te ) );
		this.container = (ContainerPatternTerm) this.inventorySlots;
		this.setReservedSpace( 81 );
	}

	@Override
	protected void actionPerformed( final GuiButton btn )
	{
		super.actionPerformed( btn );

		try
		{

			if( this.tabCraftButton == btn )
			{
				NetworkHandler.instance().sendToServer(	new PacketValueConfig( "PatternTerminal.Mode", CRAFTMODE_ITEM_PROCESSING ) );
			}
			else if( this.tabProcessButton == btn)
			{
				NetworkHandler.instance().sendToServer(	new PacketValueConfig( "PatternTerminal.Mode", CRAFTMODE_FLUID_PROCESSING ) );
			}
			else if( this.tabFluidProcessButton == btn)
			{
				NetworkHandler.instance().sendToServer(	new PacketValueConfig( "PatternTerminal.Mode", CRAFTMODE_CRAFTING ) );
			}

			if( this.encodeBtn == btn )
			{
				NetworkHandler.instance().sendToServer( new PacketValueConfig( "PatternTerminal.Encode", "1" ) );
			}

			if( this.clearBtn == btn )
			{
				NetworkHandler.instance().sendToServer( new PacketValueConfig( "PatternTerminal.Clear", "1" ) );
			}

			if( this.substitutionsEnabledBtn == btn || this.substitutionsDisabledBtn == btn )
			{
				NetworkHandler.instance()
						.sendToServer(
								new PacketValueConfig( "PatternTerminal.Substitute", this.substitutionsEnabledBtn == btn ? SUBSITUTION_DISABLE : SUBSITUTION_ENABLE ) );
			}

			if ( this.itemPatternModeIIBtn == btn )
			{
				NetworkHandler.instance().sendToServer(new PacketValueConfig( "PatternTerminal.ItemPatternMode", Integer.toString( ItemPatternMode.ITEM_TO_FLUID.ordinal() ) ) );
			}
			else if( this.itemPatternModeIFBtn == btn )
			{
				NetworkHandler.instance().sendToServer(new PacketValueConfig( "PatternTerminal.ItemPatternMode", Integer.toString( ItemPatternMode.ITEM_TO_ITEM.ordinal() ) ) );
			}
			

			if ( this.fluidPatternModeFFBtn == btn )
			{
				NetworkHandler.instance().sendToServer(new PacketValueConfig( "PatternTerminal.FluidPatternMode", Integer.toString( FluidPatternMode.FLUID_TO_ITEM.ordinal() ) ) );
			}
			else if ( this.fluidPatternModeFIBtn == btn )
			{
				NetworkHandler.instance().sendToServer(new PacketValueConfig( "PatternTerminal.FluidPatternMode", Integer.toString( FluidPatternMode.FLUID_TO_FLUID.ordinal() ) ) );
			}
		}
		catch( final IOException e )
		{
			AELog.error( e );
		}
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.tabCraftButton = new GuiTabButton( this.guiLeft + 173, this.guiTop + this.ySize - 177, new ItemStack( Blocks.CRAFTING_TABLE ), GuiText.CraftingPattern
				.getLocal(), this.itemRender );
		this.buttonList.add( this.tabCraftButton );

		this.tabProcessButton = new GuiTabButton( this.guiLeft + 173, this.guiTop + this.ySize - 177, new ItemStack( Blocks.FURNACE ), GuiText.ItemProcessingPattern
				.getLocal(), this.itemRender );
		this.buttonList.add( this.tabProcessButton );

		this.tabFluidProcessButton = new GuiTabButton( this.guiLeft + 173, this.guiTop + this.ySize - 177, new ItemStack( Items.WATER_BUCKET ), GuiText.FluidProcessingPattern
				.getLocal(), this.itemRender );
		this.buttonList.add( this.tabFluidProcessButton );

		this.itemPatternModeIIBtn = new GuiImgButton( this.guiLeft + 84, this.guiTop + this.ySize - 163, Settings.ACTIONS, ItemPatternMode.ITEM_TO_ITEM );
		this.itemPatternModeIIBtn.setHalfSize( true );
		this.buttonList.add( this.itemPatternModeIIBtn );

		this.itemPatternModeIFBtn = new GuiImgButton( this.guiLeft + 84, this.guiTop + this.ySize - 163, Settings.ACTIONS, ItemPatternMode.ITEM_TO_FLUID );
		this.itemPatternModeIFBtn.setHalfSize( true );
		this.buttonList.add( this.itemPatternModeIFBtn );

		this.fluidPatternModeFFBtn = new GuiImgButton( this.guiLeft + 84, this.guiTop + this.ySize - 163, Settings.ACTIONS, FluidPatternMode.FLUID_TO_FLUID );
		this.fluidPatternModeFFBtn.setHalfSize( true );
		this.buttonList.add( this.fluidPatternModeFFBtn );

		this.fluidPatternModeFIBtn = new GuiImgButton( this.guiLeft + 84, this.guiTop + this.ySize - 163, Settings.ACTIONS, FluidPatternMode.FLUID_TO_ITEM );
		this.fluidPatternModeFIBtn.setHalfSize( true );
		this.buttonList.add( this.fluidPatternModeFIBtn );

		this.substitutionsEnabledBtn = new GuiImgButton( this.guiLeft + 84, this.guiTop + this.ySize - 163, Settings.ACTIONS, ItemSubstitution.ENABLED );
		this.substitutionsEnabledBtn.setHalfSize( true );
		this.buttonList.add( this.substitutionsEnabledBtn );

		this.substitutionsDisabledBtn = new GuiImgButton( this.guiLeft + 84, this.guiTop + this.ySize - 163, Settings.ACTIONS, ItemSubstitution.DISABLED );
		this.substitutionsDisabledBtn.setHalfSize( true );
		this.buttonList.add( this.substitutionsDisabledBtn );

		this.clearBtn = new GuiImgButton( this.guiLeft + 74, this.guiTop + this.ySize - 163, Settings.ACTIONS, ActionItems.CLOSE );
		this.clearBtn.setHalfSize( true );
		this.buttonList.add( this.clearBtn );

		this.encodeBtn = new GuiImgButton( this.guiLeft + 147, this.guiTop + this.ySize - 142, Settings.ACTIONS, ActionItems.ENCODE );
		this.buttonList.add( this.encodeBtn );
	}

	@Override
	public void drawFG( final int offsetX, final int offsetY, final int mouseX, final int mouseY )
	{
		if( this.container.isCraftingMode() )
		{
			//crafting tab
			this.tabCraftButton.visible = true;
			this.tabProcessButton.visible = false;
			this.tabFluidProcessButton.visible = false;

			if( this.container.substitute )
			{
				this.substitutionsEnabledBtn.visible = true;
				this.substitutionsDisabledBtn.visible = false;
			}
			else
			{
				this.substitutionsEnabledBtn.visible = false;
				this.substitutionsDisabledBtn.visible = true;
			}

			this.itemPatternModeIIBtn.visible = false;
			this.itemPatternModeIFBtn.visible = false;
			this.fluidPatternModeFFBtn.visible = false;
			this.fluidPatternModeFIBtn.visible = false;
		}
		else if( this.container.isFluidMode() )
		{
			//fluid processing tab
			this.tabCraftButton.visible = false;
			this.tabProcessButton.visible = false;
			this.tabFluidProcessButton.visible = true;

			this.substitutionsEnabledBtn.visible = false;
			this.substitutionsDisabledBtn.visible = false;

			this.itemPatternModeIIBtn.visible = false;
			this.itemPatternModeIFBtn.visible = false;

			switch(this.container.fluidPatternMode)
			{
				case FLUID_TO_FLUID:
					this.fluidPatternModeFFBtn.visible = true;
					this.fluidPatternModeFIBtn.visible = false;
					break;
				case FLUID_TO_ITEM:
					this.fluidPatternModeFFBtn.visible = false;
					this.fluidPatternModeFIBtn.visible = true;
					break;
			}
			
		}
		else
		{
			//item processing tab
			this.tabCraftButton.visible = false;
			this.tabProcessButton.visible = true;
			this.tabFluidProcessButton.visible = false;

			this.substitutionsEnabledBtn.visible = false;
			this.substitutionsDisabledBtn.visible = false;

			this.fluidPatternModeFFBtn.visible = false;
			this.fluidPatternModeFIBtn.visible = false;

			switch(this.container.itemPatternMode)
			{
				case ITEM_TO_ITEM:
					this.itemPatternModeIIBtn.visible = true;
					this.itemPatternModeIFBtn.visible = false;
					break;
				case ITEM_TO_FLUID:
					this.itemPatternModeIIBtn.visible = false;
					this.itemPatternModeIFBtn.visible = true;
					break;
			}
		}

		super.drawFG( offsetX, offsetY, mouseX, mouseY );
		this.fontRenderer.drawString( GuiText.PatternTerminal.getLocal(), 8, this.ySize - 96 + 2 - this.getReservedSpace(), 4210752 );
	}

	@Override
	protected String getBackground()
	{
		if( this.container.isCraftingMode() )
		{
			return BACKGROUND_CRAFTING_MODE;
		}

		return BACKGROUND_PROCESSING_MODE;
	}

	@Override
	protected void repositionSlot( final AppEngSlot s )
	{
		final int offsetPlayerSide = s.isPlayerSide() ? 5 : 3;

		s.yPos = s.getY() + this.ySize - 78 - offsetPlayerSide;
	}
}
