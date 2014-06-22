package com.code972.elasticsearch.analysis;

import org.apache.lucene.analysis.CommonGramsFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.hebrew.HebrewTokenizer;
import org.apache.lucene.analysis.hebrew.StreamLemmasFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by synhershko on 12/25/13.
 */
public class HebrewIndexingAnalyzer extends HebrewAnalyzer {
    public HebrewIndexingAnalyzer() throws IOException {
    }

    @Override
    protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
        // on indexing we should always keep both the stem and marked original word
        // will ignore $ && will always output all lemmas + origin word$
        // basically, if analyzerType == AnalyzerType.INDEXING)
        final StreamLemmasFilter src = new StreamLemmasFilter(reader, dictRadix, prefixesTree, SPECIAL_TOKENIZATION_CASES, commonWords, lemmaFilter);
        src.setCustomWords(customWords);
        src.setKeepOriginalWord(true);

        TokenStream tok = new ASCIIFoldingFilter(src);
        tok = new AddSuffixFilter(tok, '$') {
            @Override
            protected void handleCurrentToken() {
                if (HebrewTokenizer.tokenTypeSignature(HebrewTokenizer.TOKEN_TYPES.Hebrew).equals(typeAtt.type())) {
                    if (keywordAtt.isKeyword())
                        suffixCurrent();
                    return;
                }

                if (CommonGramsFilter.GRAM_TYPE.equals(typeAtt.type()) ||
                        HebrewTokenizer.tokenTypeSignature(HebrewTokenizer.TOKEN_TYPES.Numeric).equals(typeAtt.type()) ||
                        HebrewTokenizer.tokenTypeSignature(HebrewTokenizer.TOKEN_TYPES.Mixed).equals(typeAtt.type()))
                {
                    keywordAtt.setKeyword(true);
                    return;
                }

                duplicateCurrentToken();
                suffixCurrent();
            }
        };
        return new TokenStreamComponents(src, tok);
    }
}
