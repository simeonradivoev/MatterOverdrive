package com.MO.MatterOverdrive;

public class Reference 
{
	public static final String MOD_ID = "mo";
	public static final String MOD_NAME = "Matter Overdrive";
	public static final String VERSION = "1.0";
	public static final String CLIENT_PROXY_CLASS = "com.MO.MatterOverdrive.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "com.MO.MatterOverdrive.proxy.CommonProxy";
	public static final String TEXTURE_PATH_ENTITIES = "textures/entities";
	public static final String CHANNEL_NAME = MOD_ID + "_channel";

	
	/* GUI */
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

    /* GUI Textures */
    public static final String TEXTURE_ARROW_PROGRESS = Reference.PATH_ELEMENTS + "Progress_Arrow_Right.png";
    public static final String TEXTURE_ENERGY_METER = Reference.PATH_ELEMENTS + "Energy.png";
}
