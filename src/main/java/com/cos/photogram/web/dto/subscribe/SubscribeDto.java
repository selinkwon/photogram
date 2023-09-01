package com.cos.photogram.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeDto {

	private int id;
	private String userName;
	private String profileImageUrl;
	private String name;
	private Integer subscribeState;
	private Integer equalUserState;
	
}
