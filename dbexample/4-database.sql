PGDMP  !                     }         
   4-database    16.2    16.2     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    35090 
   4-database    DATABASE     �   CREATE DATABASE "4-database" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'German_Germany.1252';
    DROP DATABASE "4-database";
                postgres    false            �            1259    35091    files    TABLE     �   CREATE TABLE public.files (
    creation_date date DEFAULT CURRENT_DATE NOT NULL,
    file_name character varying(255) NOT NULL,
    file_data bytea NOT NULL
);
    DROP TABLE public.files;
       public         heap    postgres    false            �          0    35091    files 
   TABLE DATA           D   COPY public.files (creation_date, file_name, file_data) FROM stdin;
    public          postgres    false    215   �       Q           2606    35098    files files_file_name_key 
   CONSTRAINT     Y   ALTER TABLE ONLY public.files
    ADD CONSTRAINT files_file_name_key UNIQUE (file_name);
 C   ALTER TABLE ONLY public.files DROP CONSTRAINT files_file_name_key;
       public            postgres    false    215            �   j   x�3202�50�52�,I-.�+�(ጉ�07135767120J571� ����,͒�L��zR�@:t��*��3�s0i��d����&H��A��7F��� �J8�     