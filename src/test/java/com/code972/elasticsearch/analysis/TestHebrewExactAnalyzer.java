package com.code972.elasticsearch.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;

import java.io.IOException;

/**
 * Created by synhershko on 22/06/14.
 */
public class TestHebrewExactAnalyzer extends BaseTokenStreamTestCase {
    public void testRandomStrings() throws Exception {
        checkRandomData(random(), new HebrewExactAnalyzer(), 1000*RANDOM_MULTIPLIER);
    }

    public void testBasics() throws IOException {
        HebrewExactAnalyzer a = new HebrewExactAnalyzer();

        checkOneTerm(a, "בדיקה", "בדיקה$");
        checkOneTerm(a, "בדיקה$", "בדיקה$");

        // test non-hebrew
        checkOneTerm(a, "books", "books$");
        checkOneTerm(a, "book", "book$");
        checkOneTerm(a, "book$", "book$");
        checkOneTerm(a, "steven's", "steven's$");
        checkOneTerm(a, "steven\u2019s", "steven's$");
        checkOneTerm(a, "3", "3");
        //checkOneTerm(a, "steven\uFF07s", "steven");
    }
}
