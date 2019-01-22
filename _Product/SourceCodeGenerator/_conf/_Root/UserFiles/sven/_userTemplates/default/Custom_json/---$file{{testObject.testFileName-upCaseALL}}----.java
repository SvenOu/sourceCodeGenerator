testFileName: ${{testFileName}}
a: ${{a}}

a.b: ${{a.b}}

a.b.testArray: ${{a.b.testArray}}


$tp-repeat(a.b.testArray){{
/*******************/
title: $(title)
name: $(name)
/*******************/
}}
