import { type FC, useState } from "react";
import ChatButton from "@/components/chat/ChatButton";
import ChatHeader from "@/components/chat/ChatHeader";
import ChatMessages from "@/components/chat/ChatMessages";
import ChatInput from "@/components/chat/ChatInput";

const Chat: FC = () => {
  const [isChatOpened, setIsChatOpened] = useState<boolean>(false);

  return (
    <>
      <ChatButton isChatOpened={isChatOpened} setIsChatOpened={setIsChatOpened} />
      {isChatOpened && (
        <div className="absolute bottom-24 right-6 h-[30rem] w-80 rounded-2xl bg-gray-800">
          <ChatHeader />
          <ChatMessages />
          <ChatInput />
        </div>
      )}
    </>
  );
};

export default Chat;
