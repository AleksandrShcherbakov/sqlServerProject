select distinct
'GSM' as SYSTEM,
a.posname as SITE,
a.latitude as LAT,
a.longitude as LON,
a.ci as CID,
a.name as CELL,
b.LAC as LAC,
a.TAC,
a.frequency as BAND,
b.BSC,
ISNULL(NULLIF(b.uarfcnDL,null),BCCH) as CH,
b.BSIC,
null as SCR,
null as PCI,
a.azimuth as DIR,
a.HEIGHT,
a.TILT,
b.BSC as RNCID,
a.geo_zone as REGION
from pmc.bs_general_table_cr as a join pmc.cellstatus as b on a.cellname=b.cellname
where geo_zone like 'Mck%'
and b.date in
(SELECT MAX(date) FROM pmc.cellstatus where cellname=b.cellname)
and (b.ran = '2G' )
and a.ant_location = 'outdoor'
