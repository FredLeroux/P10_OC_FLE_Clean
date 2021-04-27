PGDMP          3                y           LIBRARY    11.3    12.3     @           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            A           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            B           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            C           1262    59936    LIBRARY    DATABASE     �   CREATE DATABASE "LIBRARY" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'French_France.1252' LC_CTYPE = 'French_France.1252';
    DROP DATABASE "LIBRARY";
                LIBRARY_ADMIN    false            3          0    59962    library_building 
   TABLE DATA           7   COPY libraryum.library_building (id, name) FROM stdin;
 	   libraryum          LIBRARY_ADMIN    false    197   U       5          0    60099    library_books 
   TABLE DATA           ~   COPY libraryum.library_books (id, kind, title, author, availability, library_building_fk, number_of_reservations) FROM stdin;
 	   libraryum          LIBRARY_ADMIN    false    199   �       7          0    60202    library_roles 
   TABLE DATA           9   COPY libraryum.library_roles (id, role_type) FROM stdin;
 	   libraryum          LIBRARY_ADMIN    false    201          9          0    68146    library_customers 
   TABLE DATA           �   COPY libraryum.library_customers (id, customer_email, customer_password, customer_enabled, customer_account_non_expired, customer_credentials_non_expired, library_role_fk, customer_account_non_locked, customer_auth_token) FROM stdin;
 	   libraryum          LIBRARY_ADMIN    false    203   B       ;          0    68216    library_book_loans 
   TABLE DATA           k   COPY libraryum.library_book_loans (id, return_date, postponed, returned, book_fk, customer_fk) FROM stdin;
 	   libraryum          LIBRARY_ADMIN    false    205   T       =          0    76522    library_book_reservations 
   TABLE DATA           �   COPY libraryum.library_book_reservations (id, notification_date, canceled_status, priority, library_book_fk, library_customer_fk) FROM stdin;
 	   libraryum          LIBRARY_ADMIN    false    207   2       D           0    0    library_book_loans_id_seq    SEQUENCE SET     K   SELECT pg_catalog.setval('libraryum.library_book_loans_id_seq', 36, true);
       	   libraryum          LIBRARY_ADMIN    false    204            E           0    0     library_book_reservations_id_seq    SEQUENCE SET     R   SELECT pg_catalog.setval('libraryum.library_book_reservations_id_seq', 19, true);
       	   libraryum          LIBRARY_ADMIN    false    206            F           0    0    library_books_id_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('libraryum.library_books_id_seq', 74, true);
       	   libraryum          LIBRARY_ADMIN    false    198            G           0    0    library_building_id_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('libraryum.library_building_id_seq', 5, true);
       	   libraryum          LIBRARY_ADMIN    false    196            H           0    0    library_customers_id_seq    SEQUENCE SET     I   SELECT pg_catalog.setval('libraryum.library_customers_id_seq', 3, true);
       	   libraryum          LIBRARY_ADMIN    false    202            I           0    0    library_roles_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('libraryum.library_roles_id_seq', 2, true);
       	   libraryum          LIBRARY_ADMIN    false    200            3   A   x�3�L�L���/�8���45>/�(��M��4��M,����M,�$j�&���WR������ ��*�      5   [  x���Mj�0�u|�[�I��tW�	J���Be��M�d
C��*X�d�c�������~�e�Z?o�����]:��/!F���@e�� +	H=�P@�E�-�� ���@# �uE�������@a��@��#�i@ u�vP�@�4���g ��@b@��d�t����0P}�u������H���@���ױ 7����Cףx`އ���yɽ=�)p��Ҁ7w��.n@
@܉�6�!�h"��-���������D��$xp�qO���lτ�p78��׷6�O���I�v��~��}�z��	N���}��u !�6Y�!�_���      7   !   x�3��p�OL����2�pJ�S��b���� �w<      9     x�U��n�0  �3�I�x`�lO놃bU"�YBpC-?���O�e��{�)m����S#�V��������sxn�y�[#$<�S�;��ۊ�v�*���r(�|��I�8^�Rt�a��a��
I��݌�β�O%�x���:w)�T卉��S h�ݫz_�	�y	J1V��d�{[t�'�M��k��O�~��|l����?�|Dz��f���8YBh%�R]K��(��i���3M�ZVvW�<��fL�~��� MkU%      ;   �   x�m�A� е�Ŏ�s��s��*��)�}���͖��{:ҙ(0~U2�T�J�-1�Q�t!����e�2��F���-�e�6��M�^EU?9��v�vb�3���o=���?WEm�nz��|��^�\3�����x��gXy�/�u��Dy>��(v�}$��"�z=@��]��mݞzjXGp�#QUBm�.��.�.�K-��_ �m���      =   [   x�]��� ��vL���N�7��Fc1�z�Ks����2]�q�3�Ӡ��{��qx��M�!K�E�i4�+�,/z���l�����m�|�0"�     