package com.hanmz.service.dirtyword;

import com.github.autoconf.ConfigFactory;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by hanmz on 2016/11/10.
 */
@Slf4j
public class DirtyService {
  static AhoCorasickDoubleArrayTrie<String> trie = new AhoCorasickDoubleArrayTrie<>();

  public static void main(String[] args) {
    ConfigFactory.getConfig("db-hanmz", conf -> {
      AhoCorasickDoubleArrayTrie<String> acdat = new AhoCorasickDoubleArrayTrie<>();
      TreeMap<String, String> map = Maps.newTreeMap();
      for (String s : Splitter.on(',').trimResults().omitEmptyStrings().split(conf.get("dirtyWords", ""))) {
        map.put(s, s);
      }
      acdat.build(map);
      trie = acdat;
    });
    System.out.println(trie);

    String txt = "哈哈哈 啦啦啦 haning";
    List<AhoCorasickDoubleArrayTrie<String>.Hit<String>> wordList = trie.parseText(txt);
    if (wordList.size() > 0) {
      String hits = Joiner.on(", ").join(wordList.stream().map(i -> i.value).collect(Collectors.toSet()));
      log.warn("dirty: {}, txt:{}", hits, txt);
    }

    System.out.println(!wordList.isEmpty());
  }
}
