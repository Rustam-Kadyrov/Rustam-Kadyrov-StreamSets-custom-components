/*
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.demo.streamsets.util;

import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.Label;

public class Groups {

    @GenerateResourceBundle
    public enum OneValueSource implements Label {
        ONE_VALUE("One value");
        private final String label;

        private OneValueSource(String label) {
            this.label = label;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getLabel() {
            return this.label;
        }
    }

    @GenerateResourceBundle
    public enum OxfordProcessor implements Label {
        OXFORD("Oxford");
        private final String label;

        private OxfordProcessor(String label) {
            this.label = label;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getLabel() {
            return this.label;
        }
    }

}
