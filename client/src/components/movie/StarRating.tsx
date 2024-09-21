import { FC, useState } from "react";
import { Star } from "lucide-react";

const StarRating: FC = () => {
  const [rating, setRating] = useState(0);
  const [hoverRating, setHoverRating] = useState(0);

  const handleRating = (starIndex: number) => {
    setRating(starIndex);
    // TODO: call api endpoint to rate the movie
  };

  return (
    <div className="flex flex-row text-white">
      {Array.from({ length: 5 }, (_, starIndex) => {
        starIndex++;

        return (
          <Star
            key={starIndex}
            className={`cursor-pointer ${starIndex <= (hoverRating || rating) ? "fill-amber-300" : "fill-neutral-500"}`}
            strokeWidth={0}
            onMouseEnter={() => setHoverRating(starIndex)}
            onMouseLeave={() => setHoverRating(0)}
            onClick={() => handleRating(starIndex)}
          />
        );
      })}
    </div>
  );
};

export default StarRating;
