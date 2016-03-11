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
package org.terasology.grammar.world.block;

import com.google.common.collect.Maps;
import org.terasology.math.geom.Vector3i;
import org.terasology.world.block.Block;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class BlockCollection {
    private Map<Vector3i, Block> blocks;
    private Vector3i anchor = Vector3i.zero();


    public BlockCollection() {
        this.blocks = Maps.newHashMap();
    }

    public Map<Vector3i, Block> getBlocks() {
        return Collections.unmodifiableMap(blocks);
    }

    /**
     * Sets the block at the given position to the specfic block type.
     * May override an existing entry without any further notice.
     *
     * @param position the block position within the collection
     * @param block    the block to set
     */
    public void setBlock(Vector3i position, Block block) {
        blocks.put(position, block);
    }

    public void setAnchor(Vector3i anchor) {
        this.anchor = anchor;
    }

    public void merge(BlockCollection... other) {
        Map<Vector3i, Block> m = Stream.of(other).map(BlockCollection::getBlocks).collect(HashMap::new, Map::putAll, Map::putAll);
        this.blocks.putAll(m);
    }
}
