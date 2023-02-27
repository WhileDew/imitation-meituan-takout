package com.example.ruijiwaimai.dto;

import com.example.ruijiwaimai.entity.Setmeal;
import com.example.ruijiwaimai.entity.SetmealDish;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
