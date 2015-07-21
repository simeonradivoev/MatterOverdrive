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

package matteroverdrive.gui;

import matteroverdrive.api.network.IMatterNetworkBroadcaster;
import matteroverdrive.api.network.IMatterNetworkConnection;
import matteroverdrive.container.ContainerMachine;
import matteroverdrive.container.MOBaseContainer;
import matteroverdrive.gui.pages.MatterNetworkConfigPage;
import matteroverdrive.machines.MOTileEntityMachine;
import matteroverdrive.machines.components.ComponentMatterNetworkConfigs;

/**
 * Created by Simeon on 7/17/2015.
 */
public abstract class MOGuiNetworkMachine<T extends MOTileEntityMachine & IMatterNetworkBroadcaster & IMatterNetworkConnection> extends MOGuiMachine<T> {

    public MOGuiNetworkMachine(ContainerMachine<T> container, T machine) {
        super(container, machine);
    }

    public MOGuiNetworkMachine(ContainerMachine<T> container, T machine, int width, int height) {
        super(container, machine, width, height);
    }

    public void registerPages(MOBaseContainer container,T machine)
    {
        super.registerPages(container, machine);
        MatterNetworkConfigPage configPage = new MatterNetworkConfigPage(this,0,0,xSize,ySize,machine.getComponent(ComponentMatterNetworkConfigs.class));
        configPage.setName("Configurations");

        pages.set(1,configPage);
    }
}
