package test;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
	public static void main(String[] args) {

		String content = "<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:1.75em;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<br />\n</p>\n<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<img src=\"http://mmbiz.qpic.cn/mmbiz/G2cxnyH10RtB6Hlo4gmPmfXPicHMLO1ndW4nBDneomibWQ32kteoWCWgNUSvUHu7tSfsMZzrP1aEuOgoAYeeulQg/0?tp=webp&amp;wxfrom=5&amp;wx_lazy=1\" style=\"margin:0px;padding:0px;height:auto !important;max-width:100%;box-sizing:border-box !important;word-wrap:break-word !important;line-height:1.6;width:auto !important;visibility:visible !important;\" /> \n</p>\n<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<br />\n</p>\n<section class=\"\" style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;white-space:normal;border:0px none;word-wrap:break-word !important;background-color:#FFFFFF;\"><section style=\"margin:0.8em 0px 0.5em;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;line-height:32px;font-weight:bold;\"><section style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;display:inline-block;float:left;width:32px;height:32px;border-radius:32px;vertical-align:top;text-align:center;border-color:#EB6794;color:#FFFFFF;background-color:#EB6794;\"><section style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;display:table;width:32px;color:inherit;border-color:#EB6794;\"><section class=\"\" title=\"\" style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;display:table-cell;vertical-align:middle;border-color:#EB6794;\">3</section></section></section><section class=\"\" style=\"margin:0px 0px 0px 36px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;color:#EB6794;border-color:#EB6794;\"><span style=\"margin:0px;padding:0px;max-width:100%;font-size:15px;box-sizing:border-box !important;\">允许宝宝按照自己的步伐调整黏度</span></section></section></section>\n<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<span style=\"margin:0px;padding:0px;max-width:100%;color:#595959;font-size:15px;line-height:1.75em;box-sizing:border-box !important;\">有些宝宝天性偏内向，对新环境和陌生人很慢热，会表现得比一般孩子更黏人，就是要抓着你的手、躲在你怀里。千万别强迫他“脱离”你的怀抱，这只会让内向的娃极其没有安全感。</span> \n</p>\n<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:1.75em;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<span style=\"margin:0px;padding:0px;max-width:100%;font-size:15px;color:#595959;box-sizing:border-box !important;\">年糕就是非常内秀的孩子，糕妈觉得最好的办法就是允许他按照自己的步伐行动，<span style=\"margin:0px;padding:0px;max-width:100%;color:#000000;box-sizing:border-box !important;\"><strong style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box !important;word-wrap:break-word !important;\">多给他一些时间与耐心，让他黏着你，慢慢地适应新环境或陌生人</strong></span>。可以尝试从怀里轻轻放下宝宝，改成握住他的小手，让他在你的陪伴之下渐渐变勇敢。</span> \n</p>\n<p style=\"margin:0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<br />\n</p>\n<section class=\"\" style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;white-space:normal;border:0px none;word-wrap:break-word !important;background-color:#FFFFFF;\"><section style=\"margin:0.8em 0px 0.5em;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;line-height:32px;font-weight:bold;\"><section style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;display:inline-block;float:left;width:32px;height:32px;border-radius:32px;vertical-align:top;text-align:center;border-color:#EB6794;color:#FFFFFF;background-color:#EB6794;\"><section style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;display:table;width:32px;color:inherit;border-color:#EB6794;\"><section class=\"\" title=\"\" style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;display:table-cell;vertical-align:middle;border-color:#EB6794;\">4</section></section></section><section class=\"\" style=\"margin:0px 0px 0px 36px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;color:#EB6794;border-color:#EB6794;\"><span style=\"margin:0px;padding:0px;max-width:100%;font-size:15px;box-sizing:border-box !important;\">确保自己不会过分“黏”着宝宝</span></section></section></section>\n<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<span style=\"margin:0px;padding:0px;max-width:100%;color:#595959;font-size:15px;line-height:1.75em;box-sizing:border-box !important;\">有时，妈妈会默默享受宝宝对自己这种无条件信任的、全身心的依恋，这种独一无二的被需要的感觉会让人无法自拔。<span style=\"margin:0px;padding:0px;max-width:100%;line-height:1.75em;color:#000000;box-sizing:border-box !important;\"><strong style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box !important;word-wrap:break-word !important;\">如果不懂得适时撤退和放手，从心理上无法成长与毕业的妈妈，是会把事情弄糟的</strong></span>（论“妈宝”的起源）。</span> \n</p>\n<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:1.75em;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<span style=\"margin:0px;padding:0px;max-width:100%;font-size:15px;color:#595959;box-sizing:border-box !important;\">当宝宝已经能够从你怀抱中安心走出的时候，你需要了解自己在宝宝独立过程中扮演的角色，确保自己不会迷恋被需要的感觉。听起来有点残忍，但看着孩子独立就要哭哭啼啼的妈妈，是不是一点都不酷？</span> \n</p>\n<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<img src=\"http://mmbiz.qpic.cn/mmbiz/G2cxnyH10RtB6Hlo4gmPmfXPicHMLO1ndYZZvw67FGcHSkCicTdwrEzSbprCmSf2vS7uoI0SudO1wia3g4Ndbx35w/0?tp=webp&amp;wxfrom=5&amp;wx_lazy=1\" style=\"margin:0px;padding:0px;height:auto !important;max-width:100%;box-sizing:border-box !important;word-wrap:break-word !important;width:auto !important;visibility:visible !important;\" /> \n</p>\n<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<br />\n</p>\n<section class=\"\" style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;white-space:normal;border:0px none;word-wrap:break-word !important;background-color:#FFFFFF;\"><section class=\"\" style=\"margin:0px auto;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;\"><section style=\"margin:0px;padding:2px 10px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;\"><section style=\"margin:0px 10px 0px 0px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;width:50px;height:50px;border:2px solid #EB6794;border-radius:50%;float:left;\"><img border=\"0\" height=\"\" title=\"2.jpg\" width=\"\" src=\"http://mmbiz.qpic.cn/mmbiz/G2cxnyH10RvekjpPafTfpScghBFDhMtW5re030eC1ckSxNHGSPict550iahbESQCvkSwZqqL86h1Jgtp62Voiaoqg/0?tp=webp&amp;wxfrom=5&amp;wx_lazy=1\" style=\"margin:0px;padding:0px;height:auto !important;max-width:100%;box-sizing:border-box !important;word-wrap:break-word !important;border-radius:50%;width:auto !important;visibility:visible !important;\" /></section><section class=\"\" style=\"margin:5px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;\">\n<p style=\"margin:0px;padding:0px;max-width:100%;clear:none;min-height:1em;font-size:12px;line-height:1.5em;box-sizing:border-box !important;\">\n\t<br />\n</p>\n<p style=\"margin:0px;padding:0px;max-width:100%;clear:none;min-height:1em;font-size:12px;line-height:1.5em;box-sizing:border-box !important;\">\n\t<span style=\"margin:0px;padding:0px;max-width:100%;color:#EB6794;box-sizing:border-box !important;\"><strong style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box !important;word-wrap:break-word !important;\"><span style=\"margin:0px;padding:0px;max-width:100%;font-size:18px;box-sizing:border-box !important;\">糕妈说</span></strong></span> \n</p>\n</section></section><section style=\"margin:5px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;border:none;\"><section style=\"margin:0px 0px 0px 18px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;width:0px;border-bottom-width:0.6em;border-bottom-style:solid;border-top-color:#EB6794;border-bottom-color:#EB6794;height:10px;color:inherit;border-left-width:0.7em !important;border-left-style:solid !important;border-left-color:transparent !important;border-right-width:0.7em !important;border-right-style:solid !important;border-right-color:transparent !important;\"></section><section style=\"margin:-8px 0px 0px 18px;padding:0px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;width:0px;border-bottom-width:0.6em;border-bottom-style:solid;border-top-color:#FEFEFE;border-bottom-color:#FEFEFE;height:10px;color:inherit;float:left;border-left-width:0.7em !important;border-left-style:solid !important;border-left-color:transparent !important;border-right-width:0.7em !important;border-right-style:solid !important;border-right-color:transparent !important;\"></section><section class=\"\" style=\"margin:0px auto -2px;padding:10px;max-width:100%;box-sizing:border-box;word-wrap:break-word !important;text-align:center;border:2px solid #EB6794;border-radius:5px;\">\n<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;border-color:#EB6794;text-align:left;line-height:1.75em;box-sizing:border-box !important;\">\n\t<span style=\"margin:0px;padding:0px;max-width:100%;color:#595959;font-size:15px;line-height:1.75em;text-align:center;box-sizing:border-box !important;\">一般到了2岁之后，随着独立意识的逐渐建立，宝宝的黏人现象就会明显缓解。比如最近年糕大人就常常把老妈子抛弃在一边，自己一个人玩得很开心，我要出门了也马上挥手再见，完全不是小时候的“树袋熊”样了。糕妈一边感叹着“娃大了不随娘”，一边又从内心深处为他正在萌生的那点儿小独立而感到骄傲，我的好日子终于来啦哇咔咔咔……</span><br />\n<span style=\"margin:0px;padding:0px;max-width:100%;color:#595959;line-height:1.75em;text-align:center;box-sizing:border-box !important;\"></span> \n</p>\n<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;line-height:1.75em;text-align:left;box-sizing:border-box !important;\">\n\t<span style=\"margin:0px;padding:0px;max-width:100%;font-size:15px;color:#595959;box-sizing:border-box !important;\">现在回想小时候一步也不肯离开的小宝宝，才知道什么叫“母爱是一场分离”。有时候，也会有一些些的伤感，但正因如此，作为母亲，也要时刻关注自己的成长才对。<span style=\"margin:0px;padding:0px;max-width:100%;color:#000000;box-sizing:border-box !important;\"><strong style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box !important;word-wrap:break-word !important;\">黏我的时候，我很享受。不黏的时候，我也不会跟在他屁股后面“求关注”；我实现我的人生理想，我的“梦想”跟他无关。</strong></span>这样做妈妈，才够酷，不是吗？</span> \n</p>\n</section></section></section></section>\n<p style=\"margin:5px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:normal;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<span style=\"margin:0px;padding:0px;max-width:100%;color:#7F7F7F;font-size:12px;box-sizing:border-box !important;\"><br />\n</span> \n</p>\n<p style=\"margin:5px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:normal;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<span style=\"margin:0px;padding:0px;max-width:100%;color:#7F7F7F;font-size:12px;box-sizing:border-box !important;\">本文观点参考：</span> \n</p>\n<p style=\"margin:5px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:normal;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<span style=\"margin:0px;padding:0px;max-width:100%;font-size:12px;color:#7F7F7F;box-sizing:border-box !important;\">《美国儿科学会育儿百科》，《海蒂育儿大百科》，Parents网站</span> \n</p>\n<p style=\"margin:0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:normal;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<br />\n</p>\n<p style=\"margin:0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:pre-wrap;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<span style=\"margin:0px;padding:0px;max-width:100%;font-size:15px;color:#595959;box-sizing:border-box !important;\">喜欢糕妈的文章吗？按下图操作方法可以看宝宝喂养、睡眠、教养、早期发展的专题文章，<span style=\"margin:0px;padding:0px;max-width:100%;color:#FF0000;box-sizing:border-box !important;\"><strong style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box !important;word-wrap:break-word !important;\"><span style=\"margin:0px;padding:0px;max-width:100%;box-sizing:border-box !important;\">还支持一键搜索</span></strong></span>哦！</span> \n</p>\n<p style=\"margin:10px 0px 0px;padding:0px;max-width:100%;clear:both;min-height:1em;white-space:pre-wrap;color:#3E3E3E;font-family:微软雅黑;font-size:16px;line-height:25.6px;box-sizing:border-box !important;background-color:#FFFFFF;\">\n\t<img src=\"27568_MP/resource/images/Tulips.jpg\" style=\"margin:0px;padding:0px;height:auto !important;max-width:100%;box-sizing:border-box !important;word-wrap:break-word !important;color:#595959;font-size:15px;line-height:1.6;width:auto !important;visibility:visible !important;\" width=\"443\" height=\"830\" title=\"Tulips\" align=\"center\" alt=\"Tulips\" /> \n</p><p><img alt= src=\"http://www.zuidaima.com/images/11/201401/20140116165317078.png\" style='height:421px; width:959px' /></p><p><img alt=\"\" src=\"http://www.zuidaima.com/images/11/201401/20140116165259325.png\" style=\"height:728px; width:1439px\" /></p><p><img src=\"http://pic3.zhimg.com/e3e3c4db741aa3325a719c9c405d9b96_b.jpg\" style=\"height:384px; width:969px\" /></p>"; 
		String imagePatternStr = "<img\\s+[^>]*?src=[\"|\']((\\w+?:?//|\\/|\\w*)[^\"]*?)[\"|\'][^>]*?>";
		Pattern imagePattern = Pattern.compile(imagePatternStr); 
		Matcher matcher = imagePattern.matcher(content);
		while (matcher.find()) { 
			// img整个标签
			String imageFragment = matcher.group(0);
			// img src中图片的url前缀
			String imageFragmentURL = matcher.group(1);
			System.out.println(" url前缀:" + imageFragmentURL ); 
			System.out.println("------------------------------------\n"); 
		}
		
		//InputStream in = getStreamByUrl("http://www.zuidaima.com/images/11/201401/20140116165317078.png");
		
		
		
	
		}
	
	
	public static InputStream getStreamByUrl(String destUrl) {  
		 HttpURLConnection httpUrl = null;  
		 URL url = null;  
		 try {
			 url = new URL(destUrl);  
			 httpUrl = (HttpURLConnection) url.openConnection();  
			 httpUrl.connect();  
			return httpUrl.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			httpUrl.disconnect();
		}
	}  
}	
