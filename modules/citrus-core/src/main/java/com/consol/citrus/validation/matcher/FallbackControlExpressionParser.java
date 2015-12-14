/*
 * Copyright 2006-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.validation.matcher;

import com.consol.citrus.exceptions.CitrusRuntimeException;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Martin Maher
 * @since 2.2.1
 */
public class FallbackControlExpressionParser implements ControlExpressionParser {
    @Override
    public List<String> extractControlValues(String controlExpression, Character delimiter) {
        Character useDelimiter = delimiter != null ? delimiter : DEFAULT_DELIMITER;
        List<String> extractedParameters = new ArrayList<>();
        if (StringUtils.hasText(controlExpression)) {
            extractParameters(controlExpression, useDelimiter, extractedParameters, 0);
            if(extractedParameters.size() == 0) {
                // if the controlExpression has text but no parameters were extracted, then assume that
                // the controlExpression itself is the only parameter
                extractedParameters.add(controlExpression);
            }
        }
        return extractedParameters;
    }

    private void extractParameters(String controlExp, Character delim, List<String> extractedParameters, int searchFrom) {
        int startParameter = controlExp.indexOf(delim, searchFrom);
        if (startParameter > -1) {
            int endParameter = controlExp.indexOf(delim, startParameter + 1);
            if (endParameter > -1) {
                String extractedParameter = controlExp.substring(startParameter + 1, endParameter);
                extractedParameters.add(extractedParameter);
                int commaSeparator = controlExp.indexOf(',', endParameter);
                if (commaSeparator > -1) {
                    this.extractParameters(controlExp, delim, extractedParameters, endParameter + 1);
                }
            } else {
                throw new CitrusRuntimeException(String.format("No matching delimiter (%s) found after position '%s' in control expression: %s", delim, endParameter, controlExp));
            }
        }
    }

}
