testFileName: ${{testFileName}}

---$if(showA > 0 && showA <10){---- a: ${{a}}  ----$endif}-----
---$if(#root.showA > 10 ){---- showA: ${{showA}}  ----$endif}-----
---$if(showB === '呵呵'){---- showB: ${{showB}}  ----$endif}-----


a.b: ${{a.b}}

a.b.testArray: ${{a.b.testArray}}


$tp-repeat(a.b.testArray){{
/*******************/
title: $(title)
name: $(name)

-----$rpif(name === 'testArray name'){---呵呵呵呵呵呵 name: $(name) ----$endrpif}----
---$rpif(name !== 'testArray name'){--哈哈哈哈---$endrpif}----
/*******************/
}}
