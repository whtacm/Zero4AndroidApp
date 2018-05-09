package com.robin.atm.entity.data;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Table;
import com.robin.atm.entity.Base;

/**
 * Created by robin on 4/18/18.
 */

@Table("ad")
public class Ad extends Core {
    @Column("adId")
    public String adId;
    @Column("adName")
    public String adName;
    @Column("advertiser")
    public String advertiser;
    @Column("adDescription")
    public String adDescription;
    @Column("mediaURL")
    public String mediaURL;
    @Column("postNavUrl")
    public String postNavUrl;
    @Column("txHash")
    public String txHash;
    @Column("issueCode")
    public String issueCode;
}
