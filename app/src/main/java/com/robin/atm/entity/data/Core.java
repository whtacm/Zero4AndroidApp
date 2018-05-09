package com.robin.atm.entity.data;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;

import java.io.Serializable;

/**
 * Created by robin on 4/25/18.
 */

public class Core implements Serializable {

    @PrimaryKey(PrimaryKey.AssignType.AUTO_INCREMENT)
    @Column("id")
    public long id;
}
