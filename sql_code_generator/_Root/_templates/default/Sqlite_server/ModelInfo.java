sessionId: ${{sessionId}}
userName: ${{userName}}

$tp-repeat(data){{
/******************** $(voClassName) ****************/
voPackageName:     $(voPackageName)
voSqlName:          $(voSqlName)
voClassName:        $(voClassName)
sqlFields:          $(sqlFields)
fields:             $(fields)
fielsImport:        $(fielsImport)
daoPackageName:     $(daoPackageName)
daoClassName:       $(daoClassName)
daoImplPackageName: $(daoImplPackageName)
daoImplClassName:   $(daoImplClassName)
testPackageName:    $(testPackageName)
testClassName:      $(testClassName)
/******************** $(voClassName) end ****************/
}}

