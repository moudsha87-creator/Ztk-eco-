package com.example.zktecocontroller

import android.app.*
import android.os.Bundle
import android.graphics.Color
import android.widget.*
import java.net.InetSocketAddress
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class MainActivity : Activity() {
 private var y=0; private var m=0; private var d=0; private var h=0; private var min=0
 private lateinit var ip:EditText; private lateinit var port:EditText; private lateinit var comm:EditText
 private lateinit var dateBtn:Button; private lateinit var timeBtn:Button; private lateinit var status:TextView
 private val blue=Color.rgb(36,99,211)
 override fun onCreate(savedInstanceState:Bundle?) { super.onCreate(savedInstanceState); val c=Calendar.getInstance(); y=c.get(1);m=c.get(2);d=c.get(5);h=c.get(11);min=c.get(12); buildUi() }
 private fun buildUi() {
  val root=LinearLayout(this).apply { orientation=1; setPadding(22,24,22,24); setBackgroundColor(Color.rgb(246,248,251)) }
  val scroll=ScrollView(this).apply { addView(root) }
  root.addView(TextView(this).apply { text="◉  ZKTeco Controller"; textSize=25f; setTextColor(Color.rgb(25,40,65)) })
  root.addView(label("MB2000 • Attendance & Clock Control"))
  root.addView(label("ATTENDANCE"))
  val row=LinearLayout(this).apply { orientation=0 }
  val ci=btn("CHECK IN",Color.rgb(35,145,85)).apply { isEnabled=false; alpha=.45f }
  val co=btn("CHECK OUT",Color.rgb(190,55,55)).apply { isEnabled=false; alpha=.45f }
  row.addView(ci,LinearLayout.LayoutParams(0,58,1f).apply { rightMargin=5 })
  row.addView(co,LinearLayout.LayoutParams(0,58,1f).apply { leftMargin=5 }); root.addView(row)
  root.addView(label("Attendance recording requires approved employee authentication integration."))
  root.addView(label("DEVICE CONNECTION"))
  ip=edit("Device IP").apply { setText("10.5.1.240") }; root.addView(ip)
  port=edit("Port").apply { setText("4370") }; root.addView(port)
  comm=edit("CommKey").apply { setText("0") }; root.addView(comm)
  root.addView(btn("CONNECT",blue).apply { setOnClickListener { connectDevice() } })
  root.addView(label("MANUAL DATE & TIME"))
  dateBtn=btn(dateText(),blue).apply { setOnClickListener {
   DatePickerDialog(this@MainActivity,{_,yy,mm,dd->y=yy;m=mm;d=dd;dateBtn.text=dateText()},y,m,d).show()
  } }; root.addView(dateBtn)
  timeBtn=btn(timeText(),blue).apply { setOnClickListener {
   TimePickerDialog(this@MainActivity,{_,hh,mi->h=hh;min=mi;timeBtn.text=timeText()},h,min,false).show()
  } }; root.addView(timeBtn)
  root.addView(btn("APPLY MANUAL DATE & TIME",Color.rgb(92,105,126)).apply { setOnClickListener {
   status.text="Selected manual device time: ${dateText()} ${timeText()}. Actual clock update requires the authorized ZKTeco time-setting protocol."
  } })
  root.addView(btn("SYNC TIME WITH PHONE",Color.rgb(240,130,0)).apply { setOnClickListener {
   val c=Calendar.getInstance(); y=c.get(1);m=c.get(2);d=c.get(5);h=c.get(11);min=c.get(12)
   dateBtn.text=dateText(); timeBtn.text=timeText()
   status.text="Phone date/time loaded: ${dateText()} ${timeText()}. Actual device sync requires the authorized ZKTeco time-setting protocol."
  } })
  status=TextView(this).apply { text="Not connected"; textSize=14f; setTextColor(Color.DKGRAY); setPadding(2,20,2,10) }; root.addView(status)
  root.addView(label("CONNECT tests TCP reachability only. Date/time controls select values; this build does not yet send a clock-setting command."))
  setContentView(scroll)
 }
 private fun dateText()=String.format(Locale.US,"%04d-%02d-%02d",y,m+1,d)
 private fun timeText():String { val c=Calendar.getInstance();c.set(1,y);c.set(2,m);c.set(5,d);c.set(11,h);c.set(12,min);return SimpleDateFormat("hh:mm a",Locale.US).format(c.time) }
 private fun label(s:String)=TextView(this).apply { text=s;textSize=12f;setTextColor(Color.rgb(95,105,120));setPadding(2,17,2,7) }
 private fun edit(s:String)=EditText(this).apply { hint=s;singleLine=true;textSize=16f;setPadding(14,7,14,7);setBackgroundColor(Color.WHITE) }
 private fun btn(s:String,c:Int)=Button(this).apply { text=s;isAllCaps=false;textSize=14f;setTextColor(Color.WHITE);backgroundTintList=android.content.res.ColorStateList.valueOf(c) }
 private fun connectDevice() {
  val host=ip.text.toString().trim(); val p=port.text.toString().toIntOrNull()
  if(host.isBlank()||p==null||p !in 1..65535){status.text="Enter a valid IP and port.";return}
  status.text="Connecting to $host:$p ..."
  thread { val msg=try { Socket().use { it.connect(InetSocketAddress(host,p),3000) }; "TCP connection successful to $host:$p. Port reachability only; no attendance or clock command sent." } catch(e:Exception) { "Connection failed: ${e.message ?: "device not reachable"}" }
   runOnUiThread { status.text=msg;status.setTextColor(if(msg.startsWith("TCP")) Color.rgb(35,125,75) else Color.rgb(180,45,45)) }
  }
 }
}
