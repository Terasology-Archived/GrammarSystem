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
package org.terasology.grammar.logic.grammar.assets;

import com.google.common.base.Preconditions;
import org.terasology.assets.AssetData;
import org.terasology.assets.ResourceUrn;
import org.terasology.grammar.logic.grammar.ProductionSystem;

public class Grammar implements AssetData {

    private ResourceUrn urn;

    private ProductionSystem productionSystem;

    //TODO: store additional header information somehow

    /**
     * A new grammar object is created with the given production system..
     *
     * @param productionSystem the production system - not null.
     */
    public Grammar(ProductionSystem productionSystem) {
        Preconditions.checkArgument(productionSystem != null);
        this.productionSystem = productionSystem;
    }

    public ProductionSystem getProductionSystem() {
        return productionSystem;
    }

    public ResourceUrn getUrn() {
        return urn;
    }

}
