/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.dmn.feel.runtime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.runners.Parameterized;
import org.kie.dmn.feel.util.EvalHelper;

import static org.kie.dmn.feel.util.DynamicTypeUtils.entry;
import static org.kie.dmn.feel.util.DynamicTypeUtils.mapOf;

public class FEELConditionsAndLoopsTest extends BaseFEELTest {

    @Parameterized.Parameters(name = "{index}: {0} ({1}) = {2}")
    public static Collection<Object[]> data() {
        final Object[][] cases = new Object[][] {
                                                //                // if expressions
                                                //                { "if true then 10+5 else 10-5", BigDecimal.valueOf( 15 ) , null},
                                                //                { "if false then \"foo\" else \"bar\"", "bar" , null},
                                                //                { "if date(\"2016-08-02\") > date(\"2015-12-25\") then \"yey\" else \"nay\"", "yey" , null},
                                                //                {"if null then \"foo\" else \"bar\"", "bar" , null },
                                                //                {"if \"xyz\" then \"foo\" else \"bar\"", "bar" , null },
                                                //                {"if true then if true then 1 else 2 else if true then 3 else 4", BigDecimal.valueOf( 1 ), null},
                                                //                {"1 + if true then 1 else 2", BigDecimal.valueOf( 2 ), null},
                                                //
                                                //                // for
                                                //                {"for x in [ 10, 20, 30 ], y in [ 1, 2, 3 ] return x * y",
                                                //                        Arrays.asList( 10, 20, 30, 20, 40, 60, 30, 60, 90 ).stream().map( x -> BigDecimal.valueOf( x ) ).collect( Collectors.toList() ),
                                                //                 null },
                                                //                {"count( for x in [1, 2, 3] return x+1 )", BigDecimal.valueOf( 3 ), null},
                {"for x in 1..3 return x+1", Arrays.asList( 1, 2, 3 ).stream().map( x -> BigDecimal.valueOf( x + 1 ) ).collect( Collectors.toList() ), null},
                {"for x in 3..1 return x+1", Arrays.asList( 3, 2, 1 ).stream().map( x -> BigDecimal.valueOf( x + 1 ) ).collect( Collectors.toList() ), null},
                {"for x in 1..1 return x+1", Arrays.asList( 1 ).stream().map( x -> BigDecimal.valueOf( x + 1 ) ).collect( Collectors.toList() ), null},
                {"for x in 1..3, y in 4..6 return [x+1, y-1]", l(l(2, 3), l(2, 4), l(2, 5), l(3, 3), l(3, 4), l(3, 5), l(4, 3), l(4, 4), l(4, 5)), null},
                {"{ a: 1, b : 3, c : for x in a..b return x+1}", mapOf(entry("a", BigDecimal.valueOf(1)), entry("b", BigDecimal.valueOf(3)),  entry("c", Arrays.asList( 1, 2, 3 ).stream().map( x -> BigDecimal.valueOf( x + 1 ) ).collect( Collectors.toList() )) ), null},
                {"{ a: 1, b : 3, c : for x in a+2..b-2 return x+1}", mapOf(entry("a", BigDecimal.valueOf(1)), entry("b", BigDecimal.valueOf(3)),  entry("c", Arrays.asList( 3, 2, 1 ).stream().map( x -> BigDecimal.valueOf( x + 1 ) ).collect( Collectors.toList() )) ), null},
                {"for i in 0..10 return if i = 0 then 1 else i * partial[-1]", Arrays.asList( 1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800 ).stream().map( x -> BigDecimal.valueOf( x ) ).collect( Collectors.toList() ), null},
                {"for x in [1, 2, 3] return x+1", Arrays.asList( 1, 2, 3 ).stream().map( x -> BigDecimal.valueOf( x + 1 ) ).collect( Collectors.toList() ), null},

                // quantified
                {"if every x in [ 1, 2, 3 ] satisfies x < 5 then \"foo\" else \"bar\"", "foo", null}

        };
        return Arrays.asList( cases );
    }

    private static List<Object> l(Object... args) {
        List<Object> coerced = new ArrayList<>();
        for ( Object a : args ) {
            coerced.add(EvalHelper.coerceNumber(a));
        }
        return coerced;
    }
}
