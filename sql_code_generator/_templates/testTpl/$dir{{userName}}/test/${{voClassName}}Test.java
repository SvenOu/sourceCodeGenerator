package ${{testPackageName}};

import ${{voPackageName}}.${{voClassName}};

public class ${{testClassName}} {
    private static Log log = LogFactory.getLog(${{testClassName}}.class);

    @Autowired
    private ${{daoClassName}} ${{daoClassName-lowerCaseFirst}};

    @Test
    @Transactional
    public void test${{daoClassName}}(){
        int tag = 0;
        ${{voClassName}} ${{voSqlName-underlineToCame}}Ori = new ${{voClassName}}();
        $tp-repeat(fields){{${{voSqlName-underlineToCame}}Ori.set$(name-underlineToCameUPCASEFirst)("$(name-underlineToCame)");
        }}
        ${{voClassName}} ${{voSqlName-underlineToCame}} = ${{daoClassName-lowerCaseFirst}}.findByKey(${{voSqlName-underlineToCame}}Ori.getKey());
        if(${{voSqlName-underlineToCame}} == null){
            ${{voSqlName-underlineToCame}} = new ${{voClassName}}();
            ${{voSqlName-underlineToCame}}.setKey(${{voSqlName-underlineToCame}}Ori.getKey());
            tag ++;
            swap${{voClassName}}s(${{voSqlName-underlineToCame}}, ${{voSqlName-underlineToCame}}Ori, tag);
            ${{daoClassName-lowerCaseFirst}}.insert(${{voSqlName-underlineToCame}});
        }
        List<${{voClassName}}> ${{voSqlName-underlineToCame}}List = ${{daoClassName-lowerCaseFirst}}.findListByKey(${{voSqlName-underlineToCame}}Ori.getKey());
        Assert.assertEquals(${{voSqlName-underlineToCame}}List.size(), 1);
        assert${{voClassName}}Equal(${{daoClassName-lowerCaseFirst}}.findByKey(${{voSqlName-underlineToCame}}Ori.getKey()), ${{voSqlName-underlineToCame}}Ori);

        tag ++;
        swap${{voClassName}}s(${{voSqlName-underlineToCame}}, ${{voSqlName-underlineToCame}}Ori, tag);
        ${{daoClassName-lowerCaseFirst}}.update(${{voSqlName-underlineToCame}});
        assert${{voClassName}}Equal(${{daoClassName-lowerCaseFirst}}.findByKey(${{voSqlName-underlineToCame}}Ori.getKey()), ${{voSqlName-underlineToCame}}Ori);

        tag++;
        swap${{voClassName}}s(${{voSqlName-underlineToCame}}, ${{voSqlName-underlineToCame}}Ori, tag);
        // todo key 不需要加入update
        ${{daoClassName-lowerCaseFirst}}.updateFields(${{voSqlName-underlineToCame}}, new String[]{$tp-repeat(sqlFields-suffixNotIncludeEnd~, ){{"$(name)"}}});
        assert${{voClassName}}Equal(${{daoClassName-lowerCaseFirst}}.findByKey(${{voSqlName-underlineToCame}}Ori.getKey()), ${{voSqlName-underlineToCame}}Ori);

        tag++;
        swap${{voClassName}}s(${{voSqlName-underlineToCame}}, ${{voSqlName-underlineToCame}}Ori, tag);
        ${{daoClassName-lowerCaseFirst}}.save(${{voSqlName-underlineToCame}});
        assert${{voClassName}}Equal(${{daoClassName-lowerCaseFirst}}.findByKey(${{voSqlName-underlineToCame}}Ori.getKey()), ${{voSqlName-underlineToCame}}Ori);

        ${{daoClassName-lowerCaseFirst}}.deleteByKey(${{voSqlName-underlineToCame}}Ori.getKey());
        Assert.assertSame(null, ${{daoClassName-lowerCaseFirst}}.findByKey(${{voSqlName-underlineToCame}}Ori.getKey()));
        ${{voSqlName-underlineToCame}}List = ${{daoClassName-lowerCaseFirst}}.findListByKey(${{voSqlName-underlineToCame}}Ori.getKey());
        Assert.assertEquals(${{voSqlName-underlineToCame}}List.size(), 0);
    }

    private void swap${{voClassName}}s(${{voClassName}} ${{voSqlName-underlineToCame}}, ${{voClassName}} ${{voSqlName-underlineToCame}}Ori, int tag) {
        // todo key 不需要加1
        $tp-repeat(fields){{${{voSqlName-underlineToCame}}Ori.set$(name-underlineToCameUPCASEFirst)(${{voSqlName-underlineToCame}}Ori.get$(name-underlineToCameUPCASEFirst)() + tag);
        }}
        // todo key 不需要set
        $tp-repeat(fields){{${{voSqlName-underlineToCame}}.set$(name-underlineToCameUPCASEFirst)(${{voSqlName-underlineToCame}}Ori.get$(name-underlineToCameUPCASEFirst)());
        }}
    }

    private void assert${{voClassName}}Equal(${{voClassName}} ${{voSqlName-underlineToCame}}, ${{voClassName}} ${{voSqlName-underlineToCame}}Ori) {
        $tp-repeat(fields){{Assert.assertEquals(${{voSqlName-underlineToCame}}.get$(name-underlineToCameUPCASEFirst)(), ${{voSqlName-underlineToCame}}Ori.get$(name-underlineToCameUPCASEFirst)());
        }}
    }
}
