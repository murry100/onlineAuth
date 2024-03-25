DROP TABLE IF EXISTS PERMISSIONS CASCADE;
DROP INDEX IF EXISTS IDX_PERMISSIONS_PERMISSION_KBN;

CREATE TABLE PERMISSIONS (
    FUNCTION_ID CHAR(8),
    PERMISSION_KBN CHAR(1),
    PERMISSION_NAME VARCHAR(255),
    METHOD VARCHAR(10),
    ENDPOINT VARCHAR(2000),
    CONSTRAINT PK_PERMISSIONS PRIMARY KEY (FUNCTION_ID, PERMISSION_KBN)
);

CREATE INDEX IDX_PERMISSIONS_PERMISSION_KBN ON PERMISSIONS (PERMISSION_KBN);


INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
            'W001',
            1,
            '検索',
            'GET',
            '/search'
         );
INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
             'W001',
             2,
             'Download',
             'GET',
             '/download'
         );
INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
             'W001',
             3,
             '更新',
             'POST',
             '/update'
         );
INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
             'W002',
             1,
             '検索',
             'GET',
             '/search'
         );
INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
             'W002',
             2,
             'Download',
             'GET',
             '/download'
         );
INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
             'W002',
             3,
             '更新',
             'POST',
             '/update'
         );
INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
             'W003',
             1,
             '検索',
             'GET',
             '/search'
         );
INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
             'W003',
             2,
             'Download',
             'GET',
             '/download'
         );
INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
             'W003',
             3,
             '更新',
             'POST',
             '/update'
         );
INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
             'W004',
             1,
             '検索',
             'GET',
             '/search'
         );
INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
             'W004',
             2,
             'Download',
             'GET',
             '/download'
         );
INSERT INTO PERMISSIONS (
    FUNCTION_ID,
    PERMISSION_KBN,
    PERMISSION_NAME,
    METHOD,
    ENDPOINT
) VALUES (
             'W004',
             3,
             '更新',
             'POST',
             '/update'
         );