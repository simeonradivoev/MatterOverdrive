package com.MO.MatterOverdrive;

import cofh.lib.gui.GuiColor;

public class Reference
{
	public static final String MOD_ID = "mo";
	public static final String MOD_NAME = "Matter Overdrive";
	public static final String VERSION = "0.3.3";
	public static final String CLIENT_PROXY_CLASS = "com.MO.MatterOverdrive.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "com.MO.MatterOverdrive.proxy.CommonProxy";
	public static final String GUI_FACTORY_CLASS = "com.MO.MatterOverdrive.gui.GuiConfigFactory";
	public static final String CHANNEL_NAME = MOD_ID + "_channel";
	public static final String VERSIONS_FILE_URL = "https://raw.githubusercontent.com/simeonradivoev/MatterOverdrive/master/Versions.txt";
	public static final String VERSIONS_FILE_URL_MIRROR = "http://simeon.co.vu/Mods/MatterOverdrive/Versions.txt";

	
	//region GUI
	public static final String PATH_GFX = "mo:textures/";
	public static final String PATH_ARMOR = PATH_GFX + "armor/";
	public static final String PATH_PARTICLE = PATH_GFX + "particle/";
	public static final String PATH_GUI = PATH_GFX + "gui/";
	public static final String PATH_BLOCKS = PATH_GFX + "blocks/";
	public static final String PATH_FX = PATH_GFX + "fx/";
	public static final String PATH_SHADERS = "mo:shaders/";
	public static final String PATH_ELEMENTS = PATH_GUI + "elements/";
	public static final String PATH_ICON = PATH_GUI + "icons/";
	public static final String PATH_ENTITIES = PATH_GFX + "entities/";
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
    public static final GuiColor COLOR_HOLO = new GuiColor(169,226,251);
	public static final GuiColor COLOR_YELLOW_STRIPES = new GuiColor(254,203,4);
	public static final GuiColor COLOR_HOLO_RED = new GuiColor(230,80,20);
	public static final GuiColor COLOR_GUI_DARK = new GuiColor(44,54,52);
	public static final GuiColor COLOR_GUI_DARKER = new GuiColor(34,40,37);
	public static final GuiColor COLOR_GUI_ENERGY = new GuiColor(224,0,0);
	//endregion

	//region Modules
	public static final int MODULE_BATTERY = 0;
	public static final int MODULE_COLOR = 1;
	public static final int MODULE_BARREL = 2;
	public static final int MODULE_SIGHTS = 3;
	public static final int MODULE_OTHER = 4;
	//end region

	//region Bionic Types
	public static final int BIONIC_HEAD = 0;
	public static final int BIONIC_ARMS = 1;
	public static final int BIONIC_LEGS = 2;
	public static final int BIONIC_CHEST = 3;
	public static final int BIONIC_OTHER = 4;
	public static final int BIONIC_BATTERY = 5;
	//endregion

	//region Andoid Stats Types
	public static final int ANDROID_STAT_TELEPORT = 0;
	public static final int ANDROID_STAT_NANOBOTS = 1;
	//endregion

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

	//region Request Packet Type
    public static final int PACKET_REQUEST_CONNECTION = 0;
	public static final int PACKET_REQUEST_PATTERN_SEARCH = 1;
    public static final int PACKET_REQUEST_NEIGHBOR_CONNECTION = 2;
    //endregion

	//region Broadcast Packet Type
	public static final int PACKET_BROADCAST_CONNECTION = 0;
	//endregion

    //region Packet Responce Type
    public static final int PACKET_RESPONCE_ERROR = -1;
    public static final int PACKET_RESPONCE_INVALID = 0;
    public static final int PACKET_RESPONCE_VALID = 1;
    //endregion

	//region machine mods
	public static final byte MODE_REDSTONE_HIGH = 0;
	public static final byte MODE_REDSTONE_LOW = 1;
	public static final byte MODE_REDSTONE_NONE = 2;
	//endregion

	//region models
	public static final String MODEL_SPHERE = Reference.PATH_MODEL_BLOCKS + "sphere.obj";
	//endregion
}
