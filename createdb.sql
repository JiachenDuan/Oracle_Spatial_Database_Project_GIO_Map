-- Drop Table
drop table building;
drop table photo;
drop table photographer;
-- Create Table
CREATE TABLE building (
  id VARCHAR2(100) PRIMARY KEY,
  name VARCHAR2(100),
  vertices NUMBER,
  shape SDO_GEOMETRY
);
CREATE TABLE photo (
  photoID VARCHAR2(100) PRIMARY KEY,
  photographerID VARCHAR2(100),
  shape SDO_GEOMETRY
);
CREATE TABLE photographer(
  photographerID VARCHAR2(100) PRIMARY KEY,
  shape SDO_GEOMETRY
);

-- update the METADATA VIEW --
-- insert into user_sdo_geom_metadata
-- 	(TABLE_NAME,COLUMN_NAME,DIMINFO,SRID)
-- values (
-- 'photo',
-- 'shape',
-- SDO_DIM_ARRAY ( -- 20 x 20 grid
--   SDO_DIM_ELEMENT('X', 0, 20, 0.005),
--   SDO_DIM_ELEMENT('Y', 0, 20, 0.005)
-- ),
-- NULL -- SRID
-- );

-- insert into user_sdo_geom_metadata
-- 	(TABLE_NAME,COLUMN_NAME,DIMINFO,SRID)
-- values (
-- 'building',
-- 'shape',
-- SDO_DIM_ARRAY ( -- 20 x 20 grid
--   SDO_DIM_ELEMENT('X', 0, 20, 0.005),
--   SDO_DIM_ELEMENT('Y', 0, 20, 0.005)
-- ),
-- NULL -- SRID
-- );

-- create INDEXES -- 
create index photo_idx
on photo(shape)
indextype is MDSYS.SPATIAL_INDEX;

create index building_idx
on building (shape)
indextype is MDSYS.SPATIAL_INDEX;

create index photographer_idx
on photographer (shape)
indextype is MDSYS.SPATIAL_INDEX;
-- insert into photo values ('Photo241',' P48',SDO_GEOMETRY(2003,NULL,NULL,SDO_ELEM_INFO_ARRAY(1,1003,1),SDO_ORDINATE_ARRAY(566,244)))
-- Insert data test
-- insert into building values (
-- 'b1',
-- 'ONE',
--  4,
-- SDO_GEOMETRY(
-- 2003,
-- NULL,
-- NULL,
-- SDO_ELEM_INFO_ARRAY(1,1003,1),
-- SDO_ORDINATE_ARRAY(226, 150, 254, 164, 240, 191, 212, 176)
-- )
-- );

--@/home/ychen5/h3/createdb.sql
