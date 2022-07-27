package com.example.mystoreapidev.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@TableName("mystore_category")
@Getter
@Setter
public class Category {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("parent_id")
    private Integer parentId;
    private String name;
    private Boolean status;
    @TableField("sort_order")
    private Integer sortOrder;

    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }
}
