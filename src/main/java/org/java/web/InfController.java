package org.java.web;

import org.java.entity.Info;
import org.java.service.InfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class InfController {

    @Autowired
    private InfService infService;

    /**
     * 初始加载和分页显示同一个方法
     * @param model
     * @return
     */
    @RequestMapping("/load")
    public String init(Model model, @RequestParam(value = "currPage",required = false) Integer currPage, HttpSession ses){



        if (currPage==null){
            //第一次加载
            currPage=1;//当前页
        }
        int size=5;//每页显示的数量

        int count=infService.getCount();//查询总数
        int maxPage=count%size==0?count/size:count/size+1;//最大页

        /*************进行高并发环境的测试**************************/
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                infService.getList(1, size);
            }
        };

        //模拟线程池的环境，指定多少个线程，可以同时并发访问
        ExecutorService executorService = Executors.newFixedThreadPool(25);
        //模拟10000个请求到达服务器
        for(int i=0;i<10000;i++){
            executorService.submit(runnable);
        }

        //根据分页加载数据
        List<Info> list=infService.getList(currPage, size);
        model.addAttribute("list", list);//存储加载后的数据


        model.addAttribute("currPage", currPage);
        model.addAttribute("size", size);
        model.addAttribute("count", count);
        model.addAttribute("maxPage", maxPage);
        ses.setAttribute("currPage", currPage);

        return  "/index";

    }

    @RequestMapping("/del/{id}/{currPage}")
    public String del(@PathVariable("id") Integer id,@PathVariable("currPage") Integer currPage){
        infService.del(id);
        return "redirect:/load?currPage="+currPage;

    }
    /**
     * 用于处理页面的跳转
     * @param target
     * @return
     */
    @GetMapping("/forward/{target}")
    public String forward(@PathVariable("target")String target){
        return target;
    }

    /**
     * 添加或者修改
     * @return
     */
    @PostMapping("/save")
    public String save(@RequestParam Map m, HttpSession ses){

        //获得id
        String id = (String) m.get("id");

        if (id.equals("")) {
            infService.add(m);//添加
        } else {
            infService.update(m);//修改
        }

        //获得当前页
        Integer currPage = (Integer) ses.getAttribute("currPage");

        return "redirect:/load?currPage="+currPage;

    }
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Integer id, Model model) {

        Info m = infService.find(id);

        model.addAttribute("m", m);

        //多选框的做法
        String[] arrs={"java","net","vp"};

        model.addAttribute("arrs", arrs);

        return "/save";
    }

}
