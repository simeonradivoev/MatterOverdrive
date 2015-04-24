package com.MO.MatterOverdrive;

import cofh.lib.gui.GuiColor;

public class Reference
{
	public static final String MOD_ID = "mo";
	public static final String MOD_NAME = "Matter Overdrive";
	public static final String VERSION = "1.0";
	public static final String CLIENT_PROXY_CLASS = "com.MO.MatterOverdrive.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "com.MO.MatterOverdrive.proxy.CommonProxy";
	public static final String TEXTURE_PATH_ENTITIES = "textures/entities";
	public static final String CHANNEL_NAME = MOD_ID + "_channel";

	
	//region GUI
	public static final String PATH_GFX = "mo:textures/";
	public static final String PATH_ARMOR = PATH_GFX + "armor/";
	public static final String PATH_PARTICLE = PATH_GFX + "particle/";
	public static final String PATH_GUI = PATH_GFX + "gui/";
	public static final String PATH_BLOCKS = PATH_GFX + "blocks/";
	public static final String PATH_ELEMENTS = PATH_GUI + "elements/";
	public static final String PATH_ICON = PATH_GUI + "icons/";
    public static final String PATH_ITEM = PATH_GFX + "items/";
	public static final String PATH_GUI_ITEM = PATH_GUI + "items/";
    public static final String PATH_MODEL = "mo:models/";
    public static final String PATH_MODEL_BLOCKS = PATH_MODEL + "block/";
    public static final String PATH_SOUNDS = "mo:sounds/";
    public static final String PATH_SOUNDS_BLOCKS = PATH_SOUNDS + "blocks/";
	public static final String PATH_INFO = "mo:info/";
	//endregion

    //region GUI Textures
    public static final String TEXTURE_ARROW_PROGRESS = Reference.PATH_ELEMENTS + "Progress_Arrow_Right.png";
    public static final String TEXTURE_ENERGY_METER = Reference.PATH_ELEMENTS + "Energy.png";
	//endregion

	//region Colors
	public static final GuiColor COLOR_MATTER = new GuiColor(191,228,230);
	//endregion

	//region Modules
	public static final int MODULE_BATTERY = 0;
	public static final int MODULE_COLOR = 1;
	public static final int MODULE_BARREL = 2;
	public static final int MODULE_SIGHTS = 3;
	public static final int MODULE_OTHER = 4;
	//end region

	//region Weapon Stat
	public static final int WS_DAMAGE = 0;
	public static final int WS_AMMO = 1;
	public static final int WS_EFFECT = 2;
	public static final int WS_RANGE = 3;
	public static final int WS_FIRE_DAMAGE = 4;
	public static final int WS_BLOCK_DAMAGE = 5;
	public static final int WS_EXPLOSION_DAMAGE = 6;
	public static final int WS_FIRE_RATE = 7;
	//endregion

	// region Matter Network Tasks States
	public static final byte TASK_STATE_INVALID = -1;
	public static final byte TASK_STATE_UNKNOWN = 0;
	public static final byte TASK_STATE_WAITING = 1;
	public static final byte TASK_STATE_QUEUED = 2;
	public static final byte TASK_STATE_PROCESSING = 3;
	public static final byte TASK_STATE_FINISHED = 4;
	//endregion
}
