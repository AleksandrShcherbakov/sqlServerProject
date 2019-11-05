select distinct
'UMTS' as SYSTEM,
a.posname as SITE,
a.ci as CID,
a.latitude as LAT,
a.longitude as LON,
a.name as CELL,
a.LAC as LAC,
null as TAC,
a.frequency as BAND,
a.BSC as BSC,
b.uarfcndl as CH,
null as BSIC,
b.pscrambcode as SCR,
null as PCI,
a.azimuth as DIR,
a.HEIGHT,
a.TILT,
a.BSC as RNCID,
a.geo_zone as REGION
from  pmc.bs_general_table_cr as a left join (
select cellid, uarfcndownlink as uarfcndl, pscrambcode
from pmc.EXPORT_HU_UCELL_BSC6900GU
union
select cellid, uarfcndownlink , pscrambcode
from pmc.EXPORT_HU_UCELL_BSC6910UMTS
union
select utrancellid, utrandlarfcn, pscrambcode
from pmc.EXPORT_HU_eNodeBUtranExternalCell_eNodeB where rac!=237
union
select cellid, utrandlarfcn, pscrambcode
from pmc.EXPORT_HU_UTRANEXTERNALCELL_MICROBTS3900) as b
on a.ci=b.cellid
where a.vendor = 'H' and a.form = 'out' and a.ran='3G'
and a.geo_zone like 'Mck%'