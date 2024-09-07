package com.example;

import com.example.entity.Account;
import com.example.mapper.AccountMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class AdminBackendApplicationTests {

	@Autowired
	AccountMapper accountMapper;

	@Test
	void testMapper() {

	}

	@Test
		public void minWindow(String s, String t) {
		s = "ADOBECODEBANC";
		t = "ABC";
			char[] a = s.toCharArray();
			char[] b = t.toCharArray();
			int[][] ans = new int[t.length()][s.length()];
			for(int i = 0; i < t.length(); i++){
				for(int j = 0; j < s.length(); j++){
					if(b[i] == a[j])
						ans[i][j] = 1;
					else{
						ans[i][j] = 0;
					}
				}
			}
			int p = 0;
			int q = 0;
			StringBuilder m = new StringBuilder();
			for(int i = 0; i < t.length(); i++){
				for(int j = 0; j < s.length(); j++){
					if(i < t.length() && j < s.length())
						if(ans[i][j] == 1 && ans[i + 1][j + 1] == 1)
							m.append(a[j]);
				}
			}
			System.out.println(m);
		}

}
