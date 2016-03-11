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

import org.junit.Before;
import org.junit.Test;
import org.terasology.grammar.logic.grammar.shapes.Shape;
import org.terasology.grammar.logic.grammar.shapes.TerminalShape;
import org.terasology.grammar.logic.grammar.shapes.complex.SetRule;
import org.terasology.grammar.world.block.BlockCollection;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/** Created with IntelliJ IDEA. User: tobias Date: 29.08.12 Time: 18:04 To change this template use File | Settings | File Templates. */
public class SetRuleTest {

    private final Vector3i position = new Vector3i(1, 1, 1);
    private final Vector3i dimension = new Vector3i(2, 3, 4);

    private final Vector3i negDimension = new Vector3i(2, 3, -4);

    private SetRule setRule;
    private SetRule negSetRule;
    private BlockCollection collection;
    private BlockCollection negCollection;

    @Before
    public void setUp() throws Exception {
        BlockManager blockManager = CoreRegistry.get(BlockManager.class);

        BlockUri uri = new BlockUri("some:stone");

        setRule = new SetRule(uri);
        setRule.setPosition(position);
        setRule.setDimension(dimension);

        negSetRule = new SetRule(uri);
        negSetRule.setPosition(position);
        negSetRule.setDimension(negDimension);

        collection = new BlockCollection();
        for (int x = position.x; x < position.x + dimension.x; x++) {
            for (int y = position.y; y < position.y + dimension.y; y++) {
                for (int z = position.z; z > position.z - dimension.z; z--) {
                    collection.setBlock(new Vector3i(x, y, z), blockManager.getBlock(uri));
                }
            }
        }

        negCollection = new BlockCollection();
        for (int x = position.x; x < position.x + negDimension.x; x++) {
            for (int y = position.y; y < position.y + negDimension.y; y++) {
                for (int z = position.z; z < position.z + Math.abs(negDimension.z); z++) {
                    negCollection.setBlock(new Vector3i(x, y, z), blockManager.getBlock(uri));
                }
            }
        }
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("Set ( \"some:uri\" );", setRule.toString());
    }

    @Test
    public void testSetRule() throws Exception {
        List<Shape> elements = setRule.getElements();
        assertEquals(1, elements.size());

        Shape s = elements.get(0);
        assertTrue(s instanceof TerminalShape);

        TerminalShape t = (TerminalShape) s;
        assertEquals(collection, t.getValue());
    }

    @Test
    public void testNegativeDimension() throws Exception {
        List<Shape> elements = negSetRule.getElements();
        assertEquals(1, elements.size());

        Shape s = elements.get(0);
        assertTrue(s instanceof TerminalShape);

        TerminalShape t = (TerminalShape) s;
        assertEquals(24, t.getValue().getBlocks().size());
        assertEquals(negCollection, t.getValue());
    }
}
