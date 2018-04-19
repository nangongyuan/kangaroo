package com.yuan.dao;

import com.yuan.dto.BarrageDTO;
import com.yuan.entity.BarrageDO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BarrageDao {
    Integer saveBarrage(BarrageDO barrageDO);

    List<BarrageDTO> listBarrageDTOByAid(@Param("aid") int aid, @Param("pageNumKey") int pageNum, @Param("pageSizeKey") int pageSize);

}
