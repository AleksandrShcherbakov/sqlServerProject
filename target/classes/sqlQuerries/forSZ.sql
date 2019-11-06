SELECT * from (
select
posname,
ran,
vendor,
azimuth,
frequency,
carriernum,
name,
address,
ci,
geo_zone,
uarfcndl= null
from pmc.bs_general_table_cr where ran='2G'
union
select
posname,
a.ran,
a.vendor,
azimuth,
frequency,
carriernum,
name,
address,
a.ci,
geo_zone,
case when a.ran = '2G' then null else b.uarfcnDL end as uarfcndl
from pmc.bs_general_table_cr as a left join (
select cellid, uarfcndownlink as uarfcndl, 'H'as vendor
from pmc.EXPORT_HU_UCELL_BSC6900GU
union
select cellid, uarfcndownlink, 'H'as vendor
from pmc.EXPORT_HU_UCELL_BSC6910UMTS
union
select utrancellid, utrandlarfcn,'H'as vendor
from pmc.EXPORT_HU_eNodeBUtranExternalCell_eNodeB
union
select cellid, utrandlarfcn,'H'as vendor
from pmc.EXPORT_HU_UTRANEXTERNALCELL_BTS5900
union
select cellid, utrandlarfcn,'H'as vendor
from pmc.EXPORT_HU_UTRANEXTERNALCELL_BTS3900
union
select cellid, utrandlarfcn,'H'as vendor
from pmc.EXPORT_HU_UTRANEXTERNALCELL_MICROBTS3900
union
select
case when isnumeric(cellname)=1 then cellname else 0 end as cellname, dlearfcn,'H'as vendor
from pmc.EXPORT_HU_CELL_BTS3900
union
select
case when isnumeric(cellname)=1 then cellname else 0 end as cellname,dlearfcn,'H'as vendor
from pmc.EXPORT_HU_CELL_BTS5900
union
select
case when isnumeric(cellname)=1 then cellname else 0 end as cellname, dlearfcn,'H'as vendor
from pmc.EXPORT_HU_eNodeBCell_eNodeB
union
select eutrancellfddid, earfcndl,'E'as vendor
from pmc.LTE_data_Ericsson
where isnumeric(azimuth)=1
union
select cid, uarfcndl,'E'as vendor
from pmc.umts_data_Ericsson
) as b on a.ci=b.cellid and a.vendor =b.vendor
where a.ant_location = 'outdoor'
and (a.duplexmode is null or a.duplexmode!='nb-iot')
and (a.ran='3G' or a.ran='4G')
) as T

