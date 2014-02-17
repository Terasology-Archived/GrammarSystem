/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.grammar.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * Draws a building on an AWT canvas
 * @author Martin Steiger
 */
public class BuildingRasterizer {

    /**
     * @param g
     * @param blg
     */
    public static void raster(Graphics2D g, Building blg) {
        g.setColor(Color.BLACK);
        for (BuildingPart part : blg.getParts()) {
            Shape fp = part.getFloorPlan();
            g.draw(fp);
        }

        g.setColor(Color.MAGENTA);
        for (BuildingPart part : blg.getParts()) {
            g.draw(part.getDoor());
        }
    }

}
