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

package matteroverdrive.client.render.conversation;

import matteroverdrive.api.renderer.IDialogShot;

/**
 * Created by Simeon on 8/10/2015.
 */
public abstract class DialogShot implements IDialogShot
{
    public static DialogShotClose closeUp = new DialogShotClose(1.5f,0.3f);
    public static DialogShotClose dramaticCloseUp = new DialogShotClose(1.2f, 0.3f);
    public static DialogShotWide wideNormal = new DialogShotWide(0.22f,false,1);
    public static DialogShotWide wideOpposite = new DialogShotWide(0.22f,true,1);
    public static DialogShotFromBehind fromBehindLeftClose = new DialogShotFromBehind(2,4);
    public static DialogShotFromBehind fromBehindLeftFar = new DialogShotFromBehind(3,4);
    public static DialogShotFromBehind fromBehindRightClose = new DialogShotFromBehind(2,8);
    public static DialogShotFromBehind fromBehindRightFar = new DialogShotFromBehind(3,-8);
}
