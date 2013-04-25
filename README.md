xjdatamapper
============

A Ruby style ORM for JAVA (Prototype)
============

XJDataMapper Package consists of following three classes:

1) XConfiguration - is used to setup connection with the database for once.
2) XJData -  is a class which is handling all the basic queries for performing operations with database
and extends XConfiguration.
3) XJDataMapper- is used by models for performing database operations. It extends XJData.


AS SIMPLE AS THIS:
==================
example 1)

XJData student = new student();
student.get_where(“id”,”=”,”1”);
out.println(student.getSingle(“student.scholar_id”));


HOW TO USE:
1) Simple download three files in XJDataMapper_ORM folder in this git and include them in your project
2) In xConfiguration.java set your mysql connection values
3) Create your models extending xJDataMapper and use as per mannual


For Detailed use Kindly follow the pdf provided within.