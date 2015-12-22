/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive;

import cpw.mods.fml.common.Loader;
import matteroverdrive.client.data.Color;

public class Reference
{
	public static final String MOD_ID = "mo";
	public static final String MOD_NAME = "Matter Overdrive";
	public static final String VERSION = "0.4.0-RC4";
	public static final String VERSION_DATE = "28.10.2015";
	public static final String GUI_FACTORY_CLASS = "matteroverdrive.gui.GuiConfigFactory";
	public static final String DEPEDNENCIES = "after:ThermalExpansion;after:exnihilo;after:EnderIO;";
	public static final String CLIENT_PROXY_CLASS = "matteroverdrive.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "matteroverdrive.proxy.CommonProxy";
	public static final String CHANNEL_NAME = MOD_ID + "_channel";
	public static final String CHANNEL_WEAPONS_NAME = MOD_ID + "_channel:weapons";
	public static final String CHANNEL_GUI_NAME = MOD_ID + "_channel:gui";
	public static final String VERSIONS_CHECK_URL = "http://simeon.co.vu/Mods/MatterOverdrive/wp-json/posts?type[]=mo_download&filter[posts_per_page]=1";
	public static final String DOWNLOAD_URL = "http://simeon.co.vu/Mods/MatterOverdrive/download_category/downloads/";


	//region GUI
	public static final String PATH_GFX = "mo:textures/";
	public static final String PATH_WORLD_TEXTURES = PATH_GFX + "world/";
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
	public static final String PATH_MODEL_ITEMS = PATH_MODEL + "item/";
    public static final String PATH_SOUNDS = "mo:sounds/";
    public static final String PATH_SOUNDS_BLOCKS = PATH_SOUNDS + "blocks/";
	public static final String PATH_INFO = "mo:info/";
	//endregion

    //region GUI Textures
    public static final String TEXTURE_ARROW_PROGRESS = Reference.PATH_ELEMENTS + "Progress_Arrow_Right.png";
    public static final String TEXTURE_ENERGY_METER = Reference.PATH_ELEMENTS + "Energy.png";
	//endregion

	//region Colors
	public static final Color COLOR_MATTER = new Color(191,228,230);
    public static final Color COLOR_HOLO = new Color(169,226,251);
	public static final Color COLOR_YELLOW_STRIPES = new Color(254,203,4);
	public static final Color COLOR_HOLO_RED = new Color(230,80,20);
	public static final Color COLOR_HOLO_GREEN = new Color(24,207,0);
	public static final Color COLOR_HOLO_YELLOW = new Color(252,223,116);
    public static final Color COLOR_HOLO_PURPLE = new Color(116,23,230);
	public static final Color COLOR_GUI_NORMAL = new Color(62,81,84);
	public static final Color COLOR_GUI_LIGHT = new Color(100,113,136);
	public static final Color COLOR_GUI_LIGHTER = new Color(139,126,168);
	public static final Color COLOR_GUI_DARK = new Color(44,54,52);
	public static final Color COLOR_GUI_DARKER = new Color(34,40,37);
	public static final Color COLOR_GUI_ENERGY = new Color(224,0,0);
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

	//region Weapon Stat
	public static final int WS_DAMAGE = 0;
	public static final int WS_AMMO = 1;
	public static final int WS_EFFECT = 2;
	public static final int WS_RANGE = 3;
	public static final int WS_FIRE_DAMAGE = 4;
	public static final int WS_BLOCK_DAMAGE = 5;
	public static final int WS_EXPLOSION_DAMAGE = 6;
	public static final int WS_FIRE_RATE = 7;
	public static final int WS_HEAL = 8;
	public static final int WS_MAX_HEAT = 9;
	public static final int WS_ACCURACY = 10;
	public static final int WS_SHOOT_COOLDOWN = 11;
	//endregion

	//region Request Packet Type
    public static final int PACKET_REQUEST_CONNECTION = 0;
	public static final int PACKET_REQUEST_PATTERN_SEARCH = 1;
    public static final int PACKET_REQUEST_NEIGHBOR_CONNECTION = 2;
	public static final int PACKET_REQUEST_VALID_PATTERN_DESTINATION = 3;
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
	public static final byte MODE_REDSTONE_NONE = 2;
	public static final byte MODE_REDSTONE_HIGH = 1;
	public static final byte MODE_REDSTONE_LOW = 0;
	//endregion

	//region models
	public static final String MODEL_SPHERE = Reference.PATH_MODEL_BLOCKS + "sphere.obj";
	public static final String MODEL_CHARGING_STATION = Reference.PATH_MODEL_BLOCKS + "charging_station.obj";
	public static final String MODEL_PATTERN_STORAGE = Reference.PATH_MODEL_BLOCKS + "pattern_storage.obj";
	public static final String MODEL_REPLICATOR = Reference.PATH_MODEL_BLOCKS + "replicator.obj";
	public static final String MODEL_TRITANIUM_CRATE = Reference.PATH_MODEL_BLOCKS + "tritanium_crate.obj";
	public static final String MODEL_INSCRIBER = Reference.PATH_MODEL_BLOCKS + "inscriber.obj";
	//endregion

	//region config keys
	public static final String CONFIG_KEY_REDSTONE_MODE = "redstoneMode";
	//endregion

	//region other mods
	private static Boolean eioLoaded;
	public static boolean eioLoaded() {
		if (eioLoaded == null) eioLoaded = Loader.isModLoaded("EnderIO");
		return eioLoaded;
	}
	//endregion

	//region World Geb
	public static final String CHEST_GEN_ANDROID_HOUSE = "android_house";
	public static final String WORLD_DATA_MO_GEN_POSITIONS = "MOWorldGenPositions";
	//endregion
}
