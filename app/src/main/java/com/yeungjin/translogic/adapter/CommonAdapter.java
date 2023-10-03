package com.yeungjin.translogic.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.yeungjin.translogic.object.DATABASE;

import java.util.ArrayList;

// 데이터를 불러오는 RecyclerView.Adapter 클래스의 공통 부분들을 체계화하여 작성하기 위해 만들어진 클래스
public abstract class CommonAdapter<ViewHolder extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolder> {
    protected final Context context;                        // Adapter를 생성하는 Fragment의 Context를 담아두는 필드 sendRequest() 메소드에서 사용됨
    protected ArrayList<DATABASE> data = new ArrayList<>(); // 이 클래스에 사용될 데이터를 담아둘 컨테이너 필드

    public CommonAdapter(Context context) {
        this.context = context;
        load(); // 어댑터를 선언하자마자 데이터를 불러옴
    }

    // 데이터를 담아두는 data 필드를 앞서 선언해두었으므로 getItemCount() 메소드를 미리 선언함
    @Override
    public int getItemCount() {
        return data.size();
    }

    // (검색어 X) 데이터를 처음부터 다시 불러옴
    public void reload() {
        reload(null);
    }

    // (검색어 X) 데이터를 이어서 불러옴
    public void load() {
        load(null);
    }

    // (검색어 O) 데이터를 처음부터 다시 불러옴
    public abstract void reload(CharSequence content);

    // (검색어 O) 데이터를 이어서 불러옴
    public abstract void load(CharSequence content);

    // HTTP로 전송되는 데이터를 처리하는 메소드
    // JSON 형식으로 보내온 데이터를 data 컨테이너에 담아두는 역할을 함
    protected abstract int getResponse(String response) throws Exception;

    // reload() 메소드에서 사용되는 리스너
    /* 현재까지 불러온 데이터를 삭제하고 다시 불러오기 위해 LoadListener와 구분하여 작성됨
     * 이를 굳이 구분하는 이유는 Volley 라이브러리가 데이터를 불러오는 방식 때문인데, 이렇게 따로 작성하지 않으면
     * Request의 Queue에 쌓인 요청들이 서로 겹쳐 잘못된 데이터를 불러올 수 있기 때문에 따로 작성하여 오류를 방지하기 때문 */
    public class ReloadListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            notifyItemRangeRemoved(0, data.size()); // RecyclerView 데이터 삭제 및 data 컨테이너 내부 데이터 초기화
            data.clear();

            try {
                notifyItemRangeInserted(0, getResponse(response)); // 데이터를 다시 불러오기 때문에 시작 위치는 0
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }

    // load() 메소드에서 사용되는 리스너
    public class LoadListener implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            try {
                notifyItemRangeInserted(data.size(), getResponse(response)); // 데이터를 이어서 불러오기 위해 시작 위치는 data의 크기로 설정
            } catch (Exception error) {
                error.printStackTrace();
            }
        }
    }
}
