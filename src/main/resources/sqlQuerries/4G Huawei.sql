select distinct
case a.ran when '4G' then 'LTE' end as SYSTEM,
a.posname as SITE,
a.ci as CID,
a.latitude as LAT,
a.longitude as LON,
a.name as CELL,
a.LAC as LAC,
a.TAC,
a.frequency as BAND,
a.BSC as BSC,
b.dlearfcn as CH,
null as BSIC,
null as SCR,
b.phycellid as PCI,
a.azimuth as DIR,
a.HEIGHT,
a.TILT,
a.BSC as RNCID,
a.geo_zone as REGION
from  pmc.bs_general_table_cr as a inner join
(
select
case when isnumeric(cellname)=1 then cellname else 0 end as cellname, cellid, phycellid, dlearfcn from pmc.EXPORT_HU_CELL_BTS3900
union
select
case when isnumeric(cellname)=1 then cellname else 0 end as cellname, cellid, phycellid, dlearfcn from pmc.EXPORT_HU_CELL_BTS5900
union
select
case when isnumeric(cellname)=1 then cellname else 0 end as cellname, cellid, phycellid, dlearfcn from pmc.EXPORT_HU_eNodeBCell_eNodeB
) as b
on b.cellname = a.ci
where a.vendor = 'H'
and a.form = 'out'
and a.geo_zone like 'Mck%'