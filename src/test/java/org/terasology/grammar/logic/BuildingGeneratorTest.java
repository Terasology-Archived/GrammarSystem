/*
 * Copyright 2016 MovingBlocks
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
package org.terasology.grammar.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.terasology.grammar.logic.grammar.ProductionSystem;
import org.terasology.grammar.logic.grammar.assets.Grammar;
import org.terasology.grammar.logic.grammar.shapes.Shape;
import org.terasology.grammar.logic.grammar.shapes.ShapeSymbol;
import org.terasology.grammar.logic.grammar.shapes.complex.DivideArg;
import org.terasology.grammar.logic.grammar.shapes.complex.DivideRule;
import org.terasology.grammar.logic.grammar.shapes.complex.SetRule;
import org.terasology.grammar.logic.grammar.shapes.complex.Size;
import org.terasology.grammar.logic.grammar.shapes.complex.SplitArg;
import org.terasology.grammar.logic.grammar.shapes.complex.SplitRule;
import org.terasology.grammar.world.block.BlockCollection;
import org.terasology.grammar.world.building.BuildingGenerator;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA. User: tobias Date: 28.08.12 Time: 19:07 To change this template use File | Settings | File Templates.
 */
public class BuildingGeneratorTest {
    private static Vector3i size = new Vector3i(4, 6, 6);

    private BuildingGenerator generator;

    private BlockCollection expectedBuilding;

    @Before
    public void setUp() throws Exception {
        BlockManager blockManager = CoreRegistry.get(BlockManager.class);

        // symbols used in this test
        ShapeSymbol axiom = new ShapeSymbol("axiom");
        ShapeSymbol house = new ShapeSymbol("house");
        ShapeSymbol floor = new ShapeSymbol("floor");
        ShapeSymbol roof = new ShapeSymbol("roof");
        // used Set rules
        SetRule setStone = new SetRule(new BlockUri("engine:cobblestone"));
        SetRule setPlank = new SetRule(new BlockUri("engine:plank"));
        // used split rule (split walls)
        SplitArg walls = new SplitArg(SplitArg.SplitType.WALLS, setStone);
        SplitRule splitWalls = new SplitRule(Arrays.asList(walls));
        // used divide rule
        DivideArg floorArg = new DivideArg(new Size(1f, false), floor);
        DivideArg roofArg = new DivideArg(new Size(1f, true), roof);
        List<DivideArg> divArgs = new ArrayList<DivideArg>(2);
        divArgs.add(floorArg);
        divArgs.add(roofArg);
        DivideRule divHouse = new DivideRule(divArgs, DivideRule.Direction.Y);

        // fill the map with rules
        Map<String, List<Shape>> rules = new HashMap<String, List<Shape>>();
        rules.put(axiom.getLabel(), shapeToList(house));
        rules.put(house.getLabel(), shapeToList(divHouse));
        rules.put(floor.getLabel(), shapeToList(splitWalls));
        rules.put(roof.getLabel(), shapeToList(setPlank));

        ProductionSystem system = new ProductionSystem(rules, axiom);
        Grammar grammar = new Grammar(system);


        generator = new BuildingGenerator(grammar);

        // build up block collection for expected building
        expectedBuilding = new BlockCollection();
        for (int x = 0; x < size.x; x++) {
            for (int y = 0; y < size.y; y++) {
                for (int z = 0; z < size.z; z++) {
                    if (y == size.y - 1) {
                        expectedBuilding.setBlock(new Vector3i(x, y, -z), blockManager.getBlock
                                ("engine:plank"));
                    } else if (x == 0 || x == size.x - 1 || z == 0 || z == size.z - 1) {
                        expectedBuilding.setBlock(new Vector3i(x, y, -z), blockManager.getBlock
                                ("core:CobbleStone"));
                    }
                }
            }
        }

    }

    private List<Shape> shapeToList(Shape symbol) {
        List<Shape> l = new ArrayList<Shape>(1);
        l.add(symbol);
        return l;
    }

    @Test
    public void testBuildingGenerator() throws Exception {
        BlockCollection resultingBuilding = generator.generate(size.x, size.y, size.z);

        Assert.assertEquals(expectedBuilding.getBlocks().size(), resultingBuilding.getBlocks().size());
        Assert.assertEquals(expectedBuilding, resultingBuilding);
    }
}
