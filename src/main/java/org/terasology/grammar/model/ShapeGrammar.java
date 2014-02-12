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

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Creates a building
 * @author Martin Steiger
 */
public class ShapeGrammar
{
    /**
     * @param rectangle
     * @param dir basic orientation of the building
     * @return
     */
    public Building create(Rectangle rectangle, Point dir) {
        Building bldg = new Building();
        Rectangle door = getDoor(rectangle, dir);
        BuildingPart part = new BuildingPart(rectangle, door);

        List<BuildingPart> result = Lists.newArrayList();

        for (BuildingPart bp : splitRelativeVert(part, 2)) {
            List<BuildingPart> bp2 = splitRelativeHorz(bp, 3);

            result.add(bp2.get(0));
            result.add(bp2.get(1));
            result.addAll(splitRelativeVert(bp2.get(2), 2));
        }

        for (BuildingPart bp : result) {
            bldg.add(bp);
        }

        return bldg;
    }

    private List<BuildingPart> splitRelativeHorz(BuildingPart part, int parts) {
        return splitRelative(part, parts, true);
    }

    private List<BuildingPart> splitRelativeVert(BuildingPart part, int parts) {
        return splitRelative(part, parts, false);
    }

    private List<BuildingPart> splitRelative(BuildingPart part, int parts, boolean horz)
    {
        List<BuildingPart> result = Lists.newArrayList();
        Rectangle fullRc = part.getFloorPlan();

        for (int i = 0; i < parts; i++) {
            Rectangle rc;
            Rectangle doorRc;

            if (horz) {
                int dw = fullRc.width / parts;		// TODO: compensate rounding error
                rc = new Rectangle(fullRc.x + i * dw, fullRc.y, dw, fullRc.height);
                doorRc = getDoor(rc, new Point(1, 0));
            } else {
                int dh = fullRc.height / parts;		// TODO: compensate rounding error
                rc = new Rectangle(fullRc.x, fullRc.y + i * dh, fullRc.width, dh);
                doorRc = getDoor(rc, new Point(1, 0));
            }

            BuildingPart buildingPart = new BuildingPart(rc, doorRc);
            result.add(buildingPart);
        }

        return result;
    }

    // this method exists in similar form in Cities/Rectangles.getBorder()
    private Rectangle getDoor(Rectangle rc, Point dir) {
        if (dir.x > 0) {
            return new Rectangle(rc.x + rc.width - 1, rc.y + rc.height / 2, 1, 2);
        }
        if (dir.y > 0) {
            return new Rectangle(rc.x + rc.width / 2, rc.y + rc.height - 1, 2, 1);
        }

        if (dir.x < 0) {
            return new Rectangle(rc.x, rc.y + rc.height / 2, 1, 2);
        }

        if (dir.y < 0) {
            return new Rectangle(rc.x + rc.width / 2, rc.y, 2, 1);
        }

        throw new IllegalArgumentException("dir");
    }
}
