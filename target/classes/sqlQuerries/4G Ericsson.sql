select distinct
case a.ran when '4G' then 'LTE' end as SYSTEM,
a.posname as SITE,
a.ci as CID,
a.latitude as LAT,
a.longitude as LON,
a.name as CELL,
a.LAC as LAC,
b.TAC,
a.frequency as BAND,
a.BSC as BSC,
b.earfcndl as CH,
null as BSIC,
null as SCR,
b.pci as PCI,
a.azimuth as DIR,
a.HEIGHT,
a.TILT,
a.BSC as RNCID,
a.geo_zone as REGION
from  pmc.bs_general_table_cr as a join pmc.Last_CellStatus_4G as b
on (b.enodeb_id = a.enodeb_id and b.ecell_id=a.ecell_id) where a.vendor = 'E' and a.form = 'out'
and a.geo_zone like 'Mck%'