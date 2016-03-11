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
package org.terasology.grammar.world.building;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
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
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.In;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.entity.placement.PlaceBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RegisterSystem
public class BuildingCommands extends BaseComponentSystem {

    @In
    private Console console;

    @In
    private CameraTargetSystem cameraTargetSystem;

    @In
    private WorldProvider worldProvider;

    @Command(shortDescription = "Building generation test", runOnServer = true)
    public void build() {
        console.addMessage("Starting building a default structure ...");

        BuildingGenerator generator = setUp();
        BlockCollection collection = generator.generate(3, 12, 3);

        Vector3f targetPos = cameraTargetSystem.getHitPosition();

        if (worldProvider != null) {
            worldProvider.getWorldEntity().send(new PlaceBlocks(collection.getBlocks(targetPos)));
        }

        console.addMessage("Finished ...");
    }

    @Command(shortDescription = "Place a building with specified size in front of the player",
            helpText = "Places a constructed building in front of the player. For construction, " +
                    "a predefined org.terasology.logic.grammar is used. The bounding box of the generated structure is given by the " +
                    "specified arguments.")
    public void buildWithDimension(@CommandParam(value = "width") int width, @CommandParam(value = "height") int height,
                                   @CommandParam(value = "depth") int depth) {
        StringBuilder builder = new StringBuilder();
        builder.append("Starting building generation with predefined org.terasology.logic.grammar, using the given dimensions of ");
        builder.append("width = ").append(width).append(", ");
        builder.append("height = ").append(height).append(", ");
        builder.append("depth = ").append(depth).append(".");

        console.addMessage(builder.toString());

        Vector3f targetPos = cameraTargetSystem.getHitPosition();
        BuildingGenerator generator = complexBuildingGenerator();

        console.addMessage("Created Building Generator. Starting building process ...");
        long time = System.currentTimeMillis();

        BlockCollection collection = generator.generate(width, height, depth);

        time = System.currentTimeMillis() - time;
        builder = new StringBuilder("Created collection in ");
        builder.append(time).append(" ms");
        console.addMessage(builder.toString());

        if (worldProvider != null) {
            worldProvider.getWorldEntity().send(new PlaceBlocks(collection.getBlocks(targetPos)));
        }

        console.addMessage("Finished ...");
    }

    public BuildingGenerator setUp() {
        // symbols used in this test
        ShapeSymbol axiom = new ShapeSymbol("axiom");
        ShapeSymbol house = new ShapeSymbol("house");
        ShapeSymbol floor = new ShapeSymbol("floor");
        ShapeSymbol roof = new ShapeSymbol("roof");
        // used Set rules
        SetRule setStone = new SetRule(new BlockUri("core:cobblestone"));
        SetRule setPlank = new SetRule(new BlockUri("core:plank"));
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
        rules.put(axiom.getLabel(), Arrays.<Shape>asList(house));
        rules.put(house.getLabel(), Arrays.<Shape>asList(divHouse));
        rules.put(floor.getLabel(), Arrays.<Shape>asList(splitWalls));
        rules.put(roof.getLabel(), Arrays.<Shape>asList(setPlank));

        ProductionSystem system = new ProductionSystem(rules, axiom);
        Grammar grammar = new Grammar(system);


        return new BuildingGenerator(grammar);
    }

    public static BuildingGenerator complexBuildingGenerator() {
        //================================================
        ShapeSymbol house = new ShapeSymbol("house");
        ShapeSymbol groundFloor = new ShapeSymbol("ground_floor");
        ShapeSymbol wall = new ShapeSymbol("wall");
        ShapeSymbol wallInner = new ShapeSymbol("wall_inner");
        ShapeSymbol wallWindowed = new ShapeSymbol("win_wall");
        ShapeSymbol door = new ShapeSymbol("door");
        ShapeSymbol border = new ShapeSymbol("border");
        ShapeSymbol middlePart = new ShapeSymbol("middle_part");
        ShapeSymbol middlePartWalls = new ShapeSymbol("middle_part_walls");
        //================================================
        SetRule setStone = new SetRule(new BlockUri("core:stone"));
        SetRule setCobblestone = new SetRule(new BlockUri("core:cobblestone"));
        SetRule setOaktrunk = new SetRule(new BlockUri("core:OakTrunk"));
        SetRule setGlass = new SetRule(new BlockUri("core:glass"));
        SetRule setPlank = new SetRule(new BlockUri("core:plank"));
        SetRule setAir = new SetRule(BlockManager.AIR_ID);
        //================================================
        DivideArg divideHouseGroundFloorArg = new DivideArg(new Size(3f, true), groundFloor);
        DivideArg divideHouseBorderArg = new DivideArg(new Size(1f, true), border);
        DivideArg divideHouseMiddlePartArg = new DivideArg(new Size(1f, false), middlePart);
        DivideArg divideHouseRoofArg = new DivideArg(new Size(1f, true), setPlank.clone());
        LinkedList<DivideArg> divideHouseArgs = new LinkedList<DivideArg>();
        divideHouseArgs.add(divideHouseGroundFloorArg);
        divideHouseArgs.add(divideHouseBorderArg);
        divideHouseArgs.add(divideHouseMiddlePartArg);
        divideHouseArgs.add(divideHouseRoofArg);
        DivideRule divideHouse = new DivideRule(divideHouseArgs, DivideRule.Direction.Y);

        DivideArg divideWallStone1Arg = new DivideArg(new Size(1f, true), setStone.clone());
        DivideArg divideWallInnerArg = new DivideArg(new Size(1f, false), wallInner);
        DivideArg divideWallStone2Arg = new DivideArg(new Size(1f, true), setStone.clone());
        LinkedList<DivideArg> divideWallArgs = new LinkedList<DivideArg>();
        divideWallArgs.add(divideWallStone1Arg);
        divideWallArgs.add(divideWallInnerArg);
        divideWallArgs.add(divideWallStone2Arg);
        DivideRule divideWall = new DivideRule(divideWallArgs, DivideRule.Direction.X);

        DivideArg divideWallInnerCobblestoneArg = new DivideArg(new Size(.3f, false), setCobblestone.clone());
        DivideArg divideWallInnerDoorArg = new DivideArg(new Size(1f, true), door);
        DivideArg divideWallInnerWinWallArg = new DivideArg(new Size(.7f, false), wallWindowed);
        LinkedList<DivideArg> divideWallInnerArgs = new LinkedList<DivideArg>();
        divideWallInnerArgs.add(divideWallInnerCobblestoneArg);
        divideWallInnerArgs.add(divideWallInnerDoorArg);
        divideWallInnerArgs.add(divideWallInnerWinWallArg);
        DivideRule divideWallInner = new DivideRule(divideWallInnerArgs, DivideRule.Direction.X);

        DivideArg divideWinWallBottomArg = new DivideArg(new Size(1f, true), setCobblestone.clone());
        DivideArg divideWinWallMiddleArg = new DivideArg(new Size(1f, true), setGlass.clone());
        DivideArg divideWinWallTopArg = new DivideArg(new Size(1f, true), setCobblestone.clone());
        LinkedList<DivideArg> divideWinWallArgs = new LinkedList<DivideArg>();
        divideWinWallArgs.add(divideWinWallBottomArg);
        divideWinWallArgs.add(divideWinWallMiddleArg);
        divideWinWallArgs.add(divideWinWallTopArg);
        DivideRule divideWinWall = new DivideRule(divideWinWallArgs, DivideRule.Direction.Y);

        DivideArg doorAirArg = new DivideArg(new Size(2f, true), setAir);
        DivideArg doorTopArg = new DivideArg(new Size(1f, true), setCobblestone);
        LinkedList<DivideArg> divideDoorArgs = new LinkedList<DivideArg>();
        divideDoorArgs.add(doorAirArg);
        divideDoorArgs.add(doorTopArg);
        DivideRule divideDoor = new DivideRule(divideDoorArgs, DivideRule.Direction.Y);

        DivideArg divideMiddlePartWallsLeft = new DivideArg(new Size(1f, true), setOaktrunk.clone());
        DivideArg divideMiddlePartWallsCenter = new DivideArg(new Size(1f, false), setStone.clone());
        DivideArg divideMiddlePartWallsRight = new DivideArg(new Size(1f, true), setOaktrunk.clone());
        LinkedList<DivideArg> divideMiddlePartWallsArgs = new LinkedList<DivideArg>();
        divideMiddlePartWallsArgs.add(divideMiddlePartWallsLeft);
        divideMiddlePartWallsArgs.add(divideMiddlePartWallsCenter);
        divideMiddlePartWallsArgs.add(divideMiddlePartWallsRight);
        DivideRule divideMiddlePartWalls = new DivideRule(divideMiddlePartWallsArgs, DivideRule.Direction.X);
        //================================================
        SplitArg splitWallArg = new SplitArg(SplitArg.SplitType.WALLS, wall);
        SplitRule splitGroundFloor = new SplitRule(Arrays.asList(splitWallArg));

        SplitArg splitBorderArg = new SplitArg(SplitArg.SplitType.WALLS, setOaktrunk);
        SplitRule splitBorder = new SplitRule(Arrays.asList(splitBorderArg));

        //SplitArg splitMiddlePartArg = new SplitArg(SplitArg.SplitType.WALLS, divideMiddlePartWalls);
        //SplitArg splitMiddlePartArg = new SplitArg(SplitArg.SplitType.WALLS, setCobblestone.clone());
        SplitArg splitMiddlePartArg = new SplitArg(SplitArg.SplitType.WALLS, middlePartWalls);
        SplitRule splitMiddlePart = new SplitRule(Arrays.asList(splitMiddlePartArg));
        //================================================
        Map<String, List<Shape>> rules = new HashMap<String, List<Shape>>();
        rules.put(house.getLabel(), Arrays.<Shape>asList(divideHouse));
        rules.put(groundFloor.getLabel(), Arrays.<Shape>asList(splitGroundFloor));
        rules.put(wall.getLabel(), Arrays.<Shape>asList(divideWall));
        rules.put(wallInner.getLabel(), Arrays.<Shape>asList(divideWallInner));
        rules.put(wallWindowed.getLabel(), Arrays.<Shape>asList(divideWinWall));
        rules.put(door.getLabel(), Arrays.<Shape>asList(divideDoor));
        rules.put(border.getLabel(), Arrays.<Shape>asList(splitBorder));
        rules.put(middlePart.getLabel(), Arrays.<Shape>asList(splitMiddlePart));
        rules.put(middlePartWalls.getLabel(), Arrays.<Shape>asList(divideMiddlePartWalls));

        ProductionSystem system = new ProductionSystem(rules, house);
        Grammar grammar = new Grammar(system);

        return new BuildingGenerator(grammar);
    }

    public static BuildingGenerator testGenerator() {
        Map<String, List<Shape>> rules = new HashMap<String, List<Shape>>();

        SetRule setStone = new SetRule(new BlockUri("core:stone"));

        SetRule setGlass = new SetRule(new BlockUri("core:glass"));

        DivideArg winWallStoneArg1 = new DivideArg(new Size(.5f, false), setStone);
        DivideArg winWallGlassArg = new DivideArg(new Size(1f, true), setGlass);
        DivideArg winWallStoneArg2 = new DivideArg(new Size(.5f, false), setStone);
        List<DivideArg> args = new ArrayList<DivideArg>();
        args.add(winWallStoneArg1);
        args.add(winWallGlassArg);
        args.add(winWallStoneArg2);
        DivideRule winWallDivide = new DivideRule(args, DivideRule.Direction.X);

        ShapeSymbol wall = new ShapeSymbol("wall");
        rules.put(wall.getLabel(), Arrays.<Shape>asList(winWallDivide));

        SplitArg splitWallsArgs = new SplitArg(SplitArg.SplitType.WALLS, wall);
        SplitRule splitWalls = new SplitRule(Arrays.asList(splitWallsArgs));

        ShapeSymbol floor = new ShapeSymbol("floor");
        rules.put(floor.getLabel(), Arrays.<Shape>asList(splitWalls));

        Grammar grammar = new Grammar(new ProductionSystem(rules, floor));
        return new BuildingGenerator(grammar);
    }
}
