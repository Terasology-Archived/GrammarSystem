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
package org.terasology.grammar.logic.grammar.shapes;

import org.terasology.grammar.world.block.BlockCollection;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;

/**
 * @author Tobias 'skaldarnar' Nett
 *         <p/>
 *         A TerminalShape is a shape that stands for a specifici block collection (maybe depending on its dimension). The TerminalShape's
 *         BlockCollection can be retrieved by calling the {@code getValue()} method.
 */
public class TerminalShape extends Shape {
    /** The block collection this TerminalShape represents. */
    private BlockCollection value;

    private BlockManager blockManager = CoreRegistry.get(BlockManager.class);

    /**
     * Instantiates a new TerminalShape with the given {@link org.terasology.world.block.Block}. This will generate the BlockCollection
     * value by filling its dimension with the specific block type.
     *
     * @param blockType the block type for this terminal shape symbol
     */
    public TerminalShape(BlockUri blockType) {
        value = new BlockCollection();
        for (int a = 0; a < dimension.x; a++) {
            for (int b = 0; b < dimension.y; b++) {
                for (int c = 0; c < dimension.z; c++) {
                    Vector3i pos = new Vector3i(a, b, c);
                    matrix.transformPoint(pos);
                    value.setBlock(new Vector3i(pos.x, pos.y, pos.z), blockManager.getBlock(blockType));
                }
            }
        }
    }

    /**
     * Instantiates a new TerminalShape with the specific BlockCollection. This can be used to define special BlockCollection for the
     * TerminalShape, such as pillars, doors, windows, ...
     *
     * @param value the terminal shapes blocks
     */
    public TerminalShape(BlockCollection value) {
        this.value = value;
    }

    /**
     * By this method you can retrieve the TerminalShape's value as a BlockCollection. The BlockCollection can describe special
     * constructions (such as doors or pillars) or just a cuboid of the same block type.
     *
     * @return the BlockCollection assigned to this shape
     */
    public BlockCollection getValue() {
        return value;
    }

    /**
     * Checks if two TerminalShapes are equal. Two TerminalShapes are equal iff their values are the same.
     *
     * @param o the object to compare          *
     *
     * @return {@code true} if the object is a TerminalShape with the same BlockCollection value
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TerminalShape)) {
            return false;
        }

        TerminalShape that = (TerminalShape) o;

        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public Shape clone() {
        TerminalShape clone = new TerminalShape(value);
        clone.setActive(active);
        clone.setMatrix(matrix);
        clone.setDimension(dimension);
        return clone;
    }
}
