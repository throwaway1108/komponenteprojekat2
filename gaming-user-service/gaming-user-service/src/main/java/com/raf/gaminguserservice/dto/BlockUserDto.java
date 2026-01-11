package com.raf.gaminguserservice.dto;

import javax.validation.constraints.NotNull;

public class BlockUserDto {
    @NotNull
    private Long userId;
    @NotNull
    private Boolean block;

    public BlockUserDto() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }
}