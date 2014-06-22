package com.code972.elasticsearch.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.MockTokenizer;
import org.apache.lucene.analysis.Tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by synhershko on 22/06/14.
 */
public class TestAddSuffixFilter extends BaseTokenStreamTestCase {
    Analyzer a = new Analyzer() {
        @Override
        protected TokenStreamComponents createComponents(String fieldName,
                                                         Reader reader) {
            Tokenizer t = new MockTokenizer(reader, MockTokenizer.KEYWORD, false);
            return new TokenStreamComponents(t, new AddSuffixFilter(t, '$') {
                @Override
                protected void handleCurrentToken() {
                    duplicateCurrentToken();
                    suffixCurrent();
                }
            });
        }
    };

    Analyzer a2 = new Analyzer() {
        @Override
        protected TokenStreamComponents createComponents(String fieldName,
                                                         Reader reader) {
            Tokenizer t = new MockTokenizer(reader, MockTokenizer.KEYWORD, false);
            return new TokenStreamComponents(t, new AddSuffixFilter(t, '$') {
                @Override
                protected void handleCurrentToken() {
                    suffixCurrent();
                }
            });
        }
    };

    public void testBasicTerms() throws IOException {
        assertAnalyzesTo(a, "book", new String[]{"book$", "book"});
        assertAnalyzesTo(a2, "book", new String[]{"book$"});
    }
}
