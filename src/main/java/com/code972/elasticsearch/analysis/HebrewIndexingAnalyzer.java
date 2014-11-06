package com.code972.elasticsearch.analysis;

import com.code972.hebmorph.MorphData;
import com.code972.hebmorph.datastructures.DictHebMorph;
import com.code972.hebmorph.datastructures.DictRadix;
import com.code972.hebmorph.hspell.HSpellLoader;
import org.apache.lucene.analysis.CommonGramsFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.hebrew.HebrewTokenizer;
import org.apache.lucene.analysis.hebrew.StreamLemmasFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

/**
 * Created by synhershko on 12/25/13.
 */
public class HebrewIndexingAnalyzer extends HebrewAnalyzer {
    public HebrewIndexingAnalyzer(DictHebMorph dict, DictRadix<MorphData> customWords) throws IOException {
        super(dict, customWords);
    }

    @Override
    protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
        // on indexing we should always keep both the stem and marked original word
        // will ignore $ && will always output all lemmas + origin word$
        // basically, if analyzerType == AnalyzerType.INDEXING)
        final StreamLemmasFilter src = new StreamLemmasFilter(reader, dict, SPECIAL_TOKENIZATION_CASES, commonWords, lemmaFilter);
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
                        HebrewTokenizer.tokenTypeSignature(HebrewTokenizer.TOKEN_TYPES.Mixed).equals(typeAtt.type())) {
                    keywordAtt.setKeyword(true);
                    return;
                }

                duplicateCurrentToken();
                suffixCurrent();
            }
        };
        return new TokenStreamComponents(src, tok);
    }

    public static HebrewIndexingAnalyzer getHebrewIndexingAnalyzer () throws IOException {
        DictRadix<MorphData> radix = new HSpellLoader(new File(HSpellLoader.getHspellPath()), true).loadDictionaryFromHSpellData();
        HashMap<String, Integer> prefs = HSpellLoader.readPrefixesFromFile(HSpellLoader.getHspellPath() + HSpellLoader.PREFIX_NOH);
        return new HebrewIndexingAnalyzer(new DictHebMorph(radix,prefs),null);
    }
}
