package com.picanha.chimichurri;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CryptoConfigService {
	@Value("${app.crypto.list}")
	private String[] acceptedCryptoList;

	public boolean isCryptoAssetAcceptable(String asset) {
		return Arrays.asList(acceptedCryptoList).contains(asset);
	}

	public String[] getAllAcceptedCrypto() {
		return acceptedCryptoList;
	}
}
